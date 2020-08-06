package com.sessionknip.socialnet.web.repository;

import com.sessionknip.socialnet.web.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInfoRepo extends JpaRepository<UserInfo, Long> {
    UserInfo findByEmail(String email);
    List<UserInfo> findByFirstName(String firstName);
    List<UserInfo> findByLastName(String lastName);
}
