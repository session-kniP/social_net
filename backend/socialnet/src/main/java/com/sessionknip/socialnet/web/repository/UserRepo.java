package com.sessionknip.socialnet.web.repository;

import com.sessionknip.socialnet.web.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
