package com.sessionknip.socialnet.web.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "publications")
@Data
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String theme;

    private String text;

    @Column(name = "p_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM:dd:yyyy")
    private LocalDate publicationDate;

    @Column(name = "p_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime publicationTime;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    public Publication() {}

    public Publication(String theme, String text, User author) {
        this.theme = theme;
        this.text = text;
        this.author = author;
        this.publicationDate = LocalDate.now();
        this.publicationTime = LocalTime.now();
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publication that = (Publication) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(theme, that.theme) &&
                Objects.equals(text, that.text) &&
                Objects.equals(publicationDate, that.publicationDate) &&
                Objects.equals(publicationTime, that.publicationTime) &&
                Objects.equals(author, that.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, theme, text, publicationDate, publicationTime, author);
    }
}
