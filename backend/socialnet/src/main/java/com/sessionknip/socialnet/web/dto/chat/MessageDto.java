package com.sessionknip.socialnet.web.dto.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sessionknip.socialnet.web.domain.Message;
import com.sessionknip.socialnet.web.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class MessageDto {
    private UserDto sender;
    private String text;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM:dd:yyyy")
    private LocalDate date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime time;

    public MessageDto() { }

    public MessageDto(Message message) {
        this.sender = new UserDto(message.getOwner());
        this.text = message.getText();
        this.date = message.getMessageDate();
        this.time = message.getMessageTime();
    }
}
