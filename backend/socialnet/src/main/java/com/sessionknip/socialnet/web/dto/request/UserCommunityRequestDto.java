package com.sessionknip.socialnet.web.dto.request;

import lombok.Data;

@Data
public class UserCommunityRequestDto {

    private Long id;
    private String message;

    public UserCommunityRequestDto() {
    }
}
