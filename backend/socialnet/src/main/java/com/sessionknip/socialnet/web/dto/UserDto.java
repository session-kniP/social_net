package com.sessionknip.socialnet.web.dto;

import com.sessionknip.socialnet.web.domain.Role;
import com.sessionknip.socialnet.web.domain.User;
import lombok.Data;

import java.util.Set;

@Data
public class UserDto {

    private Long id;
    private String username;
    private Set<Role> roles;
    private UserInfoDto userInfo;

    public UserDto() {
    }

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.userInfo = new UserInfoDto(user.getUserInfo());
        this.roles = user.getRoles();
    }

    @Override
    public String toString() {
        return String.format("%s '%s' %s  <%s>", userInfo.getFirstName(), username, userInfo.getLastName(), userInfo.getEmail());
    }

}
