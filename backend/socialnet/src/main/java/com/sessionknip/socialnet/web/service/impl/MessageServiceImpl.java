package com.sessionknip.socialnet.web.service.impl;

import com.sessionknip.socialnet.web.domain.Chat;
import com.sessionknip.socialnet.web.domain.Message;
import com.sessionknip.socialnet.web.repository.MessageRepo;
import com.sessionknip.socialnet.web.service.MessageService;
import com.sessionknip.socialnet.web.service.NullAndEmptyChecker;
import com.sessionknip.socialnet.web.service.exception.MessageServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("messageServiceImpl")
public class MessageServiceImpl extends NullAndEmptyChecker implements MessageService {

    private final MessageRepo messageRepo;

    public MessageServiceImpl(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    @Override
    public Message findById(Long id) throws MessageServiceException {
        Optional<Message> message = messageRepo.findById(id);
        if (message.isEmpty()) {
            throw new MessageServiceException("Can't find message with id " + id);
        }

        return message.get();
    }

    @Override
    public List<Message> findByChat(Chat chat, Integer page, Integer howMuch) {
        List<Message> messages = messageRepo.findByChatOrderByMessageDateDescMessageTimeDesc(chat, PageRequest.of(page, howMuch)).getContent();

        return messages;
    }

    @Override
    public Message findByChatLast(Chat chat) {
        List<Message> lastList = findByChat(chat, 0, 1);
        return lastList.get(0);
    }

    @Override
    public Message addMessage(Message message) throws MessageServiceException {
        if (message.getId() != null) {
            Optional<Message> candidate = messageRepo.findById(message.getId());
            if (candidate.isPresent()) {
                throw new MessageServiceException("Message with such id is already present");
            }
        }

        if (nullOrEmpty(message.getText())) {
            throw new MessageServiceException("Message text can't be null or empty");
        }

        return messageRepo.save(message);
    }

    @Override
    public void edit(Message target, Message source) throws MessageServiceException {
        if (nullOrEmpty(source.getText())) {
            throw new MessageServiceException("Message text can't be empty or null");
        }
        target.setText(source.getText());
        messageRepo.save(target);
    }

    @Override
    public void remove(Message message) {
        messageRepo.deleteById(message.getId());
    }
}
