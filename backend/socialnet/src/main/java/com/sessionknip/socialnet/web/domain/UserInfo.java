package com.sessionknip.socialnet.web.domain;

import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@Table(name = "users_info")
public class UserInfo {

    @Value("${upload.files.path}")
    private String mediaPath;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(
            mappedBy = "userInfo",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private User user;

    @Column(name = "first_name")
    private String firstName = "";

    @Column(name = "last_name")
    private String lastName = "";

    @Enumerated(value = EnumType.ORDINAL)
    private Sex sex = Sex.UNDEFINED;

    @Column(unique = true)
    private String email = null;

    private String status = "";

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_media_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private Media avatar;

    public UserInfo() {
    }

    public UserInfo(String firstName, String lastName, Sex sex, String email, String status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.email = email;
        this.status = status;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            email = null;
        }
        this.email = email;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfo userInfo = (UserInfo) o;
        return Objects.equals(id, userInfo.id) &&
                Objects.equals(firstName, userInfo.firstName) &&
                Objects.equals(lastName, userInfo.lastName) &&
                sex == userInfo.sex &&
                Objects.equals(email, userInfo.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, sex, email);
    }
}
