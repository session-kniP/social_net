package com.sessionknip.socialnet.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sessionknip.socialnet.web.domain.Publication;
import com.sessionknip.socialnet.web.dto.UserDto;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class PublicationResponseDto {

    private String theme;
    private String text;
    private UserDto author;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM:dd:yyyy")
    private LocalDate date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime time;

    public PublicationResponseDto(Publication publication) {
        this.theme = publication.getTheme();
        this.text = publication.getText();
        this.author = new UserDto(publication.getAuthor());
        this.date = publication.getPublicationDate();
        this.time = publication.getPublicationTime();
    }

}
