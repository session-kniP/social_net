package com.sessionknip.socialnet.web.dto.request;

import lombok.Data;

@Data
public class NewsRequestDto {

    private Integer page;
    private Integer howMuch;

    public NewsRequestDto() {}
}
