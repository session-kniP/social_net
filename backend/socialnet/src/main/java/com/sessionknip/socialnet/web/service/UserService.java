package com.sessionknip.socialnet.web.service;

import com.sessionknip.socialnet.web.domain.User;

import java.util.List;

public interface UserService {

    User register(User candidate);
    User findById(Long id);
    User findByUsername(String username);
    List<User> findAll();
    void delete(Long id);

}
