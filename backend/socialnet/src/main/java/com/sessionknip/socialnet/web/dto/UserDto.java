package com.sessionknip.socialnet.web.dto;

import com.sessionknip.socialnet.web.domain.Role;
import com.sessionknip.socialnet.web.domain.User;
import lombok.Data;

import java.util.Set;

@Data
public class UserDto {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Set<Role> roles;

    public UserDto(User user) {
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.roles = user.getRoles();
    }

    @Override
    public String toString() {
        return String.format("%s '%s' %s  <%s>", firstName, username, lastName, email);
    }

}
