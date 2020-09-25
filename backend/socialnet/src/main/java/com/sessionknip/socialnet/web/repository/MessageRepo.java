package com.sessionknip.socialnet.web.repository;

import com.sessionknip.socialnet.web.domain.Chat;
import com.sessionknip.socialnet.web.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepo extends JpaRepository<Message, Long> {
    Message save(Message message);
    Page<Message> findByChatOrderByMessageDateDescMessageTimeDesc(Chat chat, Pageable pageable);
}
