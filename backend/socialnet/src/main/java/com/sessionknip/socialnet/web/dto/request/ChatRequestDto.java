package com.sessionknip.socialnet.web.dto.request;

import com.sessionknip.socialnet.web.domain.ChatType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class ChatRequestDto {

    private ChatType chatType;
    private Set<Long> userIds;
    private String title;
    private String openingMessage;

    public ChatRequestDto() { }

}
