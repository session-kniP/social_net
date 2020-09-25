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
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

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
    public Chat findPrivateByUserCommunities(UserCommunity first, UserCommunity second, Integer page, Integer howMuch) throws ChatServiceException {
        List<Chat> chat = chatRepo.findPrivate(first, second, ChatType.PRIVATE, PageRequest.of(page, howMuch));
        if (chat.isEmpty()) {
            throw new ChatServiceException(String.format("Can't find private chat with user ids: %d and %d", first.getUser().getId(), second.getUser().getId()));
        }
        return chat.get(0);
    }

    @Override
    public void createChat(Chat chat) throws ChatServiceException {
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
            List<Chat> privateCandidate = chatRepo.findPrivate(users.next(), users.next(), ChatType.PRIVATE, PageRequest.of(0, 1));

            if (privateCandidate.size() > 0) {
                throw new ChatServiceException("Private chat for these users already exists");
            }

            //private chat should not have a title; in this case title of chat for each user is name of interlocutor
            chat.setTitle(null);
        }

        chatRepo.save(chat);
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
