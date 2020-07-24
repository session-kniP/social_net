package com.sessionknip.socialnet.web.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

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
    private LocalDate pDate;

    @Column(name = "p_time")
    private LocalTime pTime;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

}
