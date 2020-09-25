package com.sessionknip.socialnet.web.dto;

import lombok.Data;

@Data
public class InfoMessageDto {
    private String message;

    public InfoMessageDto(String message) {
        this.message = message;
    }

    public InfoMessageDto() { }
}
