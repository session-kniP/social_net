package com.sessionknip.socialnet.web.dto;

import lombok.Data;

@Data
public class RegRequestDto {
    private String username;
    private String password;
    private String repeatPassword;
}
