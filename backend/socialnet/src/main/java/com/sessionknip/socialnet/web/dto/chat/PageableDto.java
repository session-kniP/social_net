package com.sessionknip.socialnet.web.dto.chat;

import lombok.Data;

@Data
public class PageableDto {
    private Integer page;
    private Integer howMuch;
    public PageableDto() { }
}
