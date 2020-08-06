package com.sessionknip.socialnet.web.dto.response;

import lombok.Data;

@Data
public class LoginResponseDto {
    private String username;
    private String token;
    private String message;

    public LoginResponseDto() { }

    public LoginResponseDto(String username, String token) {
        this.username = username;
        this.token = token;
        this.message = "You logged in successfully";
    }

    public LoginResponseDto(String username, String token, String message) {
        this.username = username;
        this.token = token;
        this.message = message;
    }

    public LoginResponseDto(String message) {
        this.message = message;
    }

}
