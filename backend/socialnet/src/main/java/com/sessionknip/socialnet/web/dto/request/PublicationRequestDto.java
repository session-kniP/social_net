package com.sessionknip.socialnet.web.dto.request;

import lombok.Data;

@Data
public class PublicationRequestDto {

    private String theme;
    private String text;

    public PublicationRequestDto() { }

}
