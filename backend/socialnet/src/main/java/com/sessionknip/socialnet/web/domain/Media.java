package com.sessionknip.socialnet.web.domain;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "media")
@Data
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "media_name")
    private String mediaName;

    public Media() {
    }

    public Media(String mediaName) {
        this.mediaName = mediaName;
    }

}
