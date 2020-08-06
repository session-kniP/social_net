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

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_info_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private UserInfo userInfo = new UserInfo();

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(joinColumns = @JoinColumn(name = "user_id"))
    private Set<Role> roles;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.roles = Collections.singleton(Role.USER);
        this.userInfo = new UserInfo();
    }

    public User(String username, String password, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.userInfo = new UserInfo();
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

    @Override
    public String toString() {
        String firstName = userInfo.getFirstName();
        String lastName = userInfo.getLastName();
        if (firstName.isEmpty() || lastName.isEmpty()) {
            return username;
        }
        return String.format("%s \"%s\" %s", firstName, username, lastName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(userInfo, user.userInfo) &&
                Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, userInfo, roles);
    }
}
