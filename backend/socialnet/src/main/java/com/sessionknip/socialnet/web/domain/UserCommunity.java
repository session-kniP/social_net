package com.sessionknip.socialnet.web.domain;

import lombok.Data;
import org.hibernate.annotations.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.web.PageableDefault;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
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


    @LazyCollection(value = LazyCollectionOption.FALSE)
    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToMany
    @JoinTable(
            name = "user_subs",
            joinColumns = @JoinColumn(name = "subscription_id"),
            inverseJoinColumns = @JoinColumn(name = "subscriber_id"))
    private List<User> subscribers = new ArrayList<>();

    @LazyCollection(value = LazyCollectionOption.FALSE)
    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToMany
    @JoinTable(
            name = "user_subs",
            joinColumns = @JoinColumn(name = "subscriber_id"),
            inverseJoinColumns = @JoinColumn(name = "subscription_id")
    )
    private List<User> subscriptions = new ArrayList<>();


//    @OneToMany(mappedBy = "friendsOwner", fetch = FetchType.LAZY)
//    private List<User> userFriends = new ArrayList<>();

//    @ElementCollection(targetClass = User.class, fetch = FetchType.LAZY)
//    @CollectionTable(joinColumns = @JoinColumn(name = "user_id"))
//    private List<User> userFriends = new ArrayList<>();

    @LazyCollection(value = LazyCollectionOption.FALSE)
    @ManyToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "outgoing_friend_id"),
            inverseJoinColumns = @JoinColumn(name = "incoming_friend_id"))
    private List<User> incomingFriends = new ArrayList<>();

    @LazyCollection(value = LazyCollectionOption.FALSE)
    @ManyToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "incoming_friend_id"),
            inverseJoinColumns = @JoinColumn(name = "outgoing_friend_id"))
    private List<User> outgoingFriends = new ArrayList<>();


    public UserCommunity() {
    }


    public UserCommunity(User user) {
        this.user = user;
        this.subscribers = new ArrayList<>();
        this.subscriptions = new ArrayList<>();
        this.incomingFriends = new ArrayList<>();
        this.outgoingFriends = new ArrayList<>();
    }


    public List<User> getUserFriends() {
        return Stream.concat(incomingFriends.stream(), outgoingFriends.stream()).collect(Collectors.toList());
    }

}
