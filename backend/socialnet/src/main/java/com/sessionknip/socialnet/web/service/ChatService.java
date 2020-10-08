package com.sessionknip.socialnet.web.service;

import com.sessionknip.socialnet.web.domain.Chat;
import com.sessionknip.socialnet.web.domain.User;
import com.sessionknip.socialnet.web.domain.UserCommunity;
import com.sessionknip.socialnet.web.repository.ChatRepo;
import com.sessionknip.socialnet.web.service.exception.ChatServiceException;

import java.util.List;

public interface ChatService {

    List<Chat> findByUserCommunity(UserCommunity user, Integer page, Integer howMuch);
    Chat findById(Long id) throws ChatServiceException;
    Chat findPrivateByUserCommunities(UserCommunity first, UserCommunity second) throws ChatServiceException;
    Chat createChat(Chat chat) throws ChatServiceException;
    void edit(Chat target, Chat source) throws ChatServiceException;
    void remove(Chat chat);

}
