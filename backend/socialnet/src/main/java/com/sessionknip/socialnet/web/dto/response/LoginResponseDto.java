package com.sessionknip.socialnet.web.dto.response;

import lombok.Data;

@Data
public class LoginResponseDto {
    private Long id;
    private String username;
    private String token;
    private String message;

    public LoginResponseDto() { }

    public LoginResponseDto(Long id, String username, String token) {
        this.id = id;
        this.username = username;
        this.token = token;
        this.message = "You logged in successfully";
    }

    public LoginResponseDto(Long id, String username, String token, String message) {
        this.id = id;
        this.username = username;
        this.token = token;
        this.message = message;
    }

    public LoginResponseDto(String message) {
        this.message = message;
    }

}
