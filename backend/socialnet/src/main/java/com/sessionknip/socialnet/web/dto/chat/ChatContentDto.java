package com.sessionknip.socialnet.web.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChatContentDto {

    private Long id;
    private String title;
    private List<MessageDto> chatList;

    public ChatContentDto() {
    }

    public ChatContentDto(Long id, List<MessageDto> chatList) {
        this.id = id;
        this.chatList = chatList;
    }
}
