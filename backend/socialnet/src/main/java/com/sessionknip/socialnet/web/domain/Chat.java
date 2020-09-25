package com.sessionknip.socialnet.web.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@Table(name = "chats")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "members"})
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(value = EnumType.ORDINAL)
    private ChatType type;

    @ManyToMany
    @JoinTable(
            name = "community_chats",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "community_id")
    )
    private Set<UserCommunity> members = Collections.emptySet();

    @OneToMany(targetEntity = Message.class, fetch = FetchType.LAZY, mappedBy = "chat")
    private List<Message> messages;

    public Chat() { }

    public Chat(ChatType type, Set<UserCommunity> members) {
        this.type = type;
        this.members = members;
    }

    public Chat(ChatType type, Set<UserCommunity> members, String title) {
        this.type = type;
        this.members = members;
        this.title = title;
    }

    public Chat(ChatType type, Set<UserCommunity> members, Message openingMessage) {
        this.type = type;
        this.members = members;
        this.messages = Arrays.asList(openingMessage);
    }

    public Chat(ChatType type, Set<UserCommunity> members, String title, Message openingMessage) {
        this.type = type;
        this.members = members;
        this.title = title;
        this.messages = Arrays.asList(openingMessage);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return Objects.equals(id, chat.id) &&
                Objects.equals(title, chat.title) &&
                type == chat.type &&
                Objects.equals(messages, chat.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, type, messages);
    }
}
