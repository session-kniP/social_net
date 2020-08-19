package com.sessionknip.socialnet.web.domain;

import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_info_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private UserInfo userInfo = new UserInfo();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_community_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private UserCommunity userCommunity = new UserCommunity();

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(joinColumns = @JoinColumn(name = "user_id"))
    private Set<Role> roles;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User friendsOwner;

    public User() {
        this.userInfo = new UserInfo();
        this.userCommunity = new UserCommunity();
        this.roles = Collections.singleton(Role.USER);
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.roles = Collections.singleton(Role.USER);
        this.userInfo = new UserInfo();
        this.userCommunity = new UserCommunity();
    }

    public User(String username, String password, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.userInfo = new UserInfo();
        this.userCommunity = new UserCommunity();
    }

    public void addRole(Role role) {
        if (this.roles == null) {
            roles = Collections.singleton(Role.USER);
        }
        roles.add(role);
    }

    public Set<Role> getRoles() {
        if (this.roles == null) {
            roles = Collections.singleton(Role.USER);
        }
        if (roles.size() == 0) {
            roles.add(Role.USER);
        }
        return roles;
    }

    public UserInfo getUserInfo() {
        if (userInfo == null) {
            userInfo = new UserInfo();
        }
        return userInfo;
    }

    public UserCommunity getUserCommunity() {
        if (userCommunity == null) {
            userCommunity = new UserCommunity();
        }
        return userCommunity;
    }

    @Override
    public String toString() {
        if (userInfo != null) {
            String firstName = userInfo.getFirstName();
            String lastName = userInfo.getLastName();
            if (!firstName.isEmpty() && !lastName.isEmpty()) {
                return String.format("%s \"%s\" %s", firstName, username, lastName);
            }
        }
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, roles);
    }
}
