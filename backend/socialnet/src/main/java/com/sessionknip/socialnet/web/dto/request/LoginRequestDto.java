package com.sessionknip.socialnet.web.dto.request;

import lombok.Data;

@Data
public class LoginRequestDto {

    public LoginRequestDto() { }

    private String username;
    private String password;
}
