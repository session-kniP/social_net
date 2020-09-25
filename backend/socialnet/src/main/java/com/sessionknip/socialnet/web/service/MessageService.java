package com.sessionknip.socialnet.web.service;

import com.sessionknip.socialnet.web.domain.Chat;
import com.sessionknip.socialnet.web.domain.Message;
import com.sessionknip.socialnet.web.service.exception.MessageServiceException;

import java.util.List;

public interface MessageService {

    Message findById(Long id) throws MessageServiceException;
    List<Message> findByChat(Chat chat, Integer page, Integer howMuch);
    Message findByChatLast(Chat chat);
    Message addMessage(Message message) throws MessageServiceException;
    void edit(Message target, Message source) throws MessageServiceException;
    void remove(Message message);

}
