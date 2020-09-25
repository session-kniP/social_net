package com.sessionknip.socialnet.web.controller.chat;

import com.sessionknip.socialnet.web.domain.*;
import com.sessionknip.socialnet.web.dto.InfoMessageDto;
import com.sessionknip.socialnet.web.dto.UserDto;
import com.sessionknip.socialnet.web.dto.chat.ChatContentDto;
import com.sessionknip.socialnet.web.dto.chat.ChatInfoDto;
import com.sessionknip.socialnet.web.dto.chat.MessageDto;
import com.sessionknip.socialnet.web.dto.request.ChatRequestDto;
import com.sessionknip.socialnet.web.security.UserDetailsImpl;
import com.sessionknip.socialnet.web.security.WebSocketPrincipal;
import com.sessionknip.socialnet.web.service.ChatService;
import com.sessionknip.socialnet.web.service.MessageService;
import com.sessionknip.socialnet.web.service.UserCommunityService;
import com.sessionknip.socialnet.web.service.exception.ChatServiceException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/community/chats")
public class ChatController {

    private final MessageSendingOperations<String> template;

    private final ChatService chatService;
    private final MessageService messageService;
    private final UserCommunityService communityService;

    public ChatController(
            MessageSendingOperations<String> template,
            @Qualifier("chatServiceImpl") ChatService chatService,
            @Qualifier("messageServiceImpl") MessageService messageService,
            @Qualifier("userCommunityServiceImpl") UserCommunityService communityService
    ) {
        this.template = template;
        this.chatService = chatService;
        this.messageService = messageService;
        this.communityService = communityService;
    }


    @GetMapping
    public ResponseEntity<List<ChatInfoDto>> getChats(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer howMuch
    ) {
        page = page == null ? 0 : page;
        howMuch = howMuch == null ? 10 : howMuch;

        UserCommunity userCommunity = communityService.getByUser(userDetails.getUser());
        List<Chat> userChats = chatService.findByUserCommunity(userCommunity, page, howMuch);

        List<ChatInfoDto> chatInfoDto = userChats.stream().map(chat -> {
            Message lastMessage = messageService.findByChatLast(chat);
            return new ChatInfoDto(chat.getId(), new UserDto(lastMessage.getOwner()), lastMessage.getText());
        }).collect(Collectors.toList());

        return new ResponseEntity<>(chatInfoDto, HttpStatus.OK);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<?> getChat(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long chatId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer howMuch
    ) {
        page = page == null ? 0 : page;
        howMuch = howMuch == null ? 10 : howMuch;

        try {
            Chat chat = chatService.findById(chatId);
            List<MessageDto> messages =
                    messageService.findByChat(chat, page, howMuch).stream()
                            .map(message -> new MessageDto(new UserDto(message.getOwner()), message.getText(), message.getMessageDate(), message.getMessageTime()))
                            .collect(Collectors.toList());

            return new ResponseEntity<>(new ChatContentDto(chat.getTitle(), messages), HttpStatus.OK);
        } catch (ChatServiceException e) {
            return new ResponseEntity<>(new InfoMessageDto(e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/private/{userId}")
    public ResponseEntity<?> getPrivateChat(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long userId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer howMuch
    ) {
        page = page == null ? 0 : page;
        howMuch = howMuch == null ? 10 : howMuch;

        UserCommunity second = communityService.getByUserId(userId);
        try {
            Chat chat = chatService.findPrivateByUserCommunities(userDetails.getUser().getUserCommunity(), second, page, howMuch);
            List<MessageDto> messages = chat.getMessages().stream()
                    .map(m -> new MessageDto(
                            new UserDto(m.getOwner()),
                            m.getText(),
                            m.getMessageDate(),
                            m.getMessageTime()))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(new ChatContentDto(messages), HttpStatus.OK);
        } catch (ChatServiceException e) {
            return new ResponseEntity<>(new InfoMessageDto(e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/createChat")
    public ResponseEntity<?> createChat(
            Principal principal,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody ChatRequestDto chatRequest
    ) {
        Chat chat;

        Set<UserCommunity> communities = chatRequest.getUserIds()
                .stream().map(communityService::getByUserId).collect(Collectors.toSet());

        communities.add(communityService.getByUserId(userDetails.getUser().getId()));

        if (chatRequest.getTitle() == null) {
            chat = new Chat(chatRequest.getChatType(), communities);
        } else {
            chat = new Chat(chatRequest.getChatType(), communities, chatRequest.getTitle());
        }

        try {
            chatService.createChat(chat);
        } catch (ChatServiceException e) {
            return new ResponseEntity<>(new InfoMessageDto(e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @MessageMapping("/{chatId}/sendMessage")
    @SendToUser("/")
    public void sendMessage(
            WebSocketPrincipal principal,
            @DestinationVariable Long chatId,
            @Payload MessageDto messageDto
    ) throws Exception {
//        System.out.println(userDetails.getUsername() + " : " + message.getText());
        User user = principal.getUserDetails().getUser();
        Message message = new Message(MessageType.MESSAGE, user, messageDto.getText(), chatService.findById(chatId));

        messageDto = new MessageDto(message);
        messageService.addMessage(message);
        template.convertAndSend(String.format("/chatBroker/%s", chatId), messageDto);
    }

}
