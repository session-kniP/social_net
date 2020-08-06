package com.sessionknip.socialnet.web.dto.request;

import lombok.Data;

@Data
public class RegRequestDto {

    public RegRequestDto() { }

    private String username;
    private String password;
    private String repeatPassword;
}
