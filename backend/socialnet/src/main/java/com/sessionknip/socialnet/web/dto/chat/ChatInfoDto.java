package com.sessionknip.socialnet.web.dto.chat;

import com.sessionknip.socialnet.web.dto.community.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatInfoDto {
    private Long id;
    private UserDto lastMessageAuthor;
    private String lastMessage;

    public ChatInfoDto() { }
}
