package com.sessionknip.socialnet.web.service.impl;

import com.sessionknip.socialnet.web.domain.Chat;
import com.sessionknip.socialnet.web.domain.ChatType;
import com.sessionknip.socialnet.web.domain.User;
import com.sessionknip.socialnet.web.domain.UserCommunity;
import com.sessionknip.socialnet.web.repository.ChatRepo;
import com.sessionknip.socialnet.web.service.ChatService;
import com.sessionknip.socialnet.web.service.NullAndEmptyChecker;
import com.sessionknip.socialnet.web.service.exception.ChatServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.PageRanges;
import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("chatServiceImpl")
public class ChatServiceImpl extends NullAndEmptyChecker implements ChatService {

    private final ChatRepo chatRepo;

    public ChatServiceImpl(ChatRepo chatRepo) {
        this.chatRepo = chatRepo;
    }

    @Override
    public List<Chat> findByUserCommunity(UserCommunity userCommunity, Integer page, Integer howMuch) {
        List<Chat> chats = chatRepo.findByUserCommunity(userCommunity, PageRequest.of(page, howMuch)).getContent();

        return chats;
    }

    @Override
    public Chat findById(Long id) throws ChatServiceException {
        Optional<Chat> chat = chatRepo.findById(id);
        if (chat.isEmpty()) {
            throw new ChatServiceException("Can't find chat with id " + id);
        }
        return chat.get();
    }

    @Override
    @Transactional
    public Chat findPrivateByUserCommunities(UserCommunity first, UserCommunity second) throws ChatServiceException {
        Optional<Chat> chat = chatRepo.findPrivate(first, second, ChatType.PRIVATE);
        if (chat.isEmpty()) {
            chat = Optional.of(new Chat(ChatType.PRIVATE, Arrays.stream(new UserCommunity[]{first, second}).collect(Collectors.toSet())));
            chatRepo.save(chat.get());
        }
        return chat.get();
    }

    @Override
    public Chat createChat(Chat chat) throws ChatServiceException {
        if (chat.getId() != null) {
            Optional<Chat> candidate = chatRepo.findById(chat.getId());
            if (candidate.isPresent()) {
                throw new ChatServiceException("Chat with such id already present");
            }
        }

        if (chat.getType() == ChatType.PRIVATE) {
            if (chat.getMembers().size() != 2) {
                throw new ChatServiceException("Private chat should only have two members");
            }

            Iterator<UserCommunity> users = chat.getMembers().iterator();

            //private chat for two unique users should be unique and single
            Optional<Chat> privateCandidate = chatRepo.findPrivate(users.next(), users.next(), ChatType.PRIVATE);

            if (privateCandidate.isPresent()) {
                throw new ChatServiceException("Private chat for these users already exists");
            }

            //private chat should not have a title; in this case title of chat for each user is name of interlocutor
            chat.setTitle(null);
        }

        return chatRepo.save(chat);
    }

    @Override
    public void edit(Chat target, Chat source) throws ChatServiceException {
        if (source.getType() == ChatType.PRIVATE) {
            if (!target.getMembers().equals(source.getMembers())) {
                throw new ChatServiceException("Set of user in private chat not editable");
            }
        } else {
            if (notNullAndNotEmpty(source.getTitle())) {
                target.setTitle(source.getTitle());
            }
            target.setMembers(source.getMembers());
        }

        chatRepo.save(target);
    }

    @Override
    public void remove(Chat chat) {
        chatRepo.deleteById(chat.getId());
    }
}
