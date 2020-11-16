package com.sessionknip.socialnet.web.service;

import com.sessionknip.socialnet.web.domain.User;
import com.sessionknip.socialnet.web.service.exception.UserServiceException;

import java.util.List;

public interface UserService {

    User register(User candidate) throws UserServiceException;
    User findById(Long id);
    User findByUsername(String username);
    List<User> findAll();
    void edit(User target, User source) throws UserServiceException;
    void update(User user);
    void delete(Long id);

}
