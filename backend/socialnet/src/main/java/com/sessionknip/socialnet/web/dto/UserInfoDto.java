package com.sessionknip.socialnet.web.dto;

import com.sessionknip.socialnet.web.domain.Sex;
import com.sessionknip.socialnet.web.domain.UserInfo;
import lombok.Data;

@Data
public class UserInfoDto {


    private String firstName;
    private String lastName;
    private Sex sex;
    private String email;

    public UserInfoDto() {}

    public UserInfoDto(UserInfo info) {
        this.firstName = info.getFirstName();
        this.lastName = info.getLastName();
        this.sex = info.getSex();
        this.email = info.getEmail();
    }

}
