package com.sessionknip.socialnet.web.domain;

import lombok.Data;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    private String password;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(joinColumns = @JoinColumn(name = "user_id"))
    private Set<Role> roles;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.roles = Collections.singleton(Role.USER);
    }

    public User(String username, String password, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.firstName = "";
        this.lastName = "";
        this.email = "";
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

    public String toString() {
        if (firstName.isEmpty() || lastName.isEmpty()) {
            return username;
        }
        return String.format("%s \"%s\" %s", firstName, username, lastName);
    }

}
