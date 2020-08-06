package com.sessionknip.socialnet.web.dto.request;

import lombok.Data;

@Data
public class ChangePasswordDto {

    private String prevPassword;
    private String newPassword;

    public ChangePasswordDto() { }

}
