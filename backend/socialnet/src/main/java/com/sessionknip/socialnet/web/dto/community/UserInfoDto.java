package com.sessionknip.socialnet.web.dto.community;

import com.sessionknip.socialnet.web.domain.Media;
import com.sessionknip.socialnet.web.domain.Sex;
import com.sessionknip.socialnet.web.domain.UserInfo;
import lombok.Data;
import org.springframework.core.io.Resource;

@Data
public class UserInfoDto {

    private String firstName;
    private String lastName;
    private Sex sex;
    private String email;
    private String status;
    private Media avatar;

    public UserInfoDto() {}

    public UserInfoDto(UserInfo info) {
        this.firstName = info.getFirstName();
        this.lastName = info.getLastName();
        this.sex = info.getSex();
        this.email = info.getEmail();
        this.status = info.getStatus();
        this.avatar = info.getAvatar();
    }

}
