package com.sessionknip.socialnet.web.domain;

import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Table(name = "user_community")
@Entity
public class UserCommunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(
            mappedBy = "userCommunity",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private User user;


    //    @LazyCollection(value = LazyCollectionOption.FALSE)
    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_subs",
            joinColumns = @JoinColumn(name = "subscription_id"),
            inverseJoinColumns = @JoinColumn(name = "subscriber_id"))
    private List<UserCommunity> subscribers = new ArrayList<>();

    //    @LazyCollection(value = LazyCollectionOption.FALSE)
    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_subs",
            joinColumns = @JoinColumn(name = "subscriber_id"),
            inverseJoinColumns = @JoinColumn(name = "subscription_id"))
    private List<UserCommunity> subscriptions = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "outgoing_friend_id"),
            inverseJoinColumns = @JoinColumn(name = "incoming_friend_id"))
    private List<UserCommunity> incomingFriends = new ArrayList<>();

    //    @LazyCollection(value = LazyCollectionOption.FALSE)
    @ManyToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "incoming_friend_id"),
            inverseJoinColumns = @JoinColumn(name = "outgoing_friend_id"))
    private List<UserCommunity> outgoingFriends = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "community_chats",
            joinColumns = @JoinColumn(name = "community_id"),
            inverseJoinColumns = @JoinColumn(name = "chat_id")
    )
    private Set<Chat> chats = Collections.emptySet();


    public UserCommunity() {
    }


    public UserCommunity(User user) {
        this.user = user;
        this.subscribers = new ArrayList<>();
        this.subscriptions = new ArrayList<>();
        this.incomingFriends = new ArrayList<>();
        this.outgoingFriends = new ArrayList<>();
    }


    public List<UserCommunity> getUserFriends() {
        return Stream.concat(incomingFriends.stream(), outgoingFriends.stream()).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserCommunity that = (UserCommunity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(user, that.user) &&
                Objects.equals(subscribers, that.subscribers) &&
                Objects.equals(subscriptions, that.subscriptions) &&
                Objects.equals(incomingFriends, that.incomingFriends) &&
                Objects.equals(outgoingFriends, that.outgoingFriends);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, subscribers, subscriptions, incomingFriends, outgoingFriends);
    }
}
