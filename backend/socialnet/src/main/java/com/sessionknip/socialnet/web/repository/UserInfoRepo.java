package com.sessionknip.socialnet.web.repository;

import com.sessionknip.socialnet.web.domain.UserInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInfoRepo extends JpaRepository<UserInfo, Long> {
    UserInfo findByEmail(String email);
    List<UserInfo> findByFirstName(String firstName);
    List<UserInfo> findByLastName(String lastName);
    List<UserInfo> findByFirstNameLikeOrLastNameLikeIgnoreCase(String firstNameLike, String lastNameLike, Pageable pageable);
}
