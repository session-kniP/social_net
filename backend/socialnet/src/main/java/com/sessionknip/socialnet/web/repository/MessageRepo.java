package com.sessionknip.socialnet.web.repository;

import com.sessionknip.socialnet.web.domain.Chat;
import com.sessionknip.socialnet.web.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepo extends JpaRepository<Message, Long> {
    Message save(Message message);
    Page<Message> findByChatOrderByMessageDateDescMessageTimeDesc(Chat chat, Pageable pageable);
}
