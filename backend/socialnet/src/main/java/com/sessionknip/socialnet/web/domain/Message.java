package com.sessionknip.socialnet.web.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Data
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private MessageType messageType;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    private User owner;

    @Column(length = 3000)
    private String text;

    @Column(name = "m_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM:dd:yyyy")
    private LocalDate messageDate;

    @Column(name = "m_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime messageTime;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    public Message() {
    }

    public Message(MessageType messageType, User owner, String text, Chat chat) {
        this.messageType = messageType;
        this.owner = owner;
        this.text = text;
        this.chat = chat;
        this.messageDate = LocalDate.now();
        this.messageTime = LocalTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id) &&
                messageType == message.messageType &&
                Objects.equals(owner, message.owner) &&
                Objects.equals(text, message.text) &&
                Objects.equals(messageDate, message.messageDate) &&
                Objects.equals(messageTime, message.messageTime) &&
                Objects.equals(chat, message.chat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, messageType, owner, text, messageDate, messageTime, chat);
    }
}
