package com.sessionknip.socialnet.web.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChatContentDto {

    private String title;
    private List<MessageDto> chatList;

    public ChatContentDto() {
    }

    public ChatContentDto(List<MessageDto> chatList) {
        this.chatList = chatList;
    }
}
