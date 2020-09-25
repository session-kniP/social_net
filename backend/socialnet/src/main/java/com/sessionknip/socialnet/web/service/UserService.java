package com.sessionknip.socialnet.web.service;

import com.sessionknip.socialnet.web.domain.User;
import com.sessionknip.socialnet.web.service.exception.UserServiceException;

import java.util.List;

public interface UserService {

    User register(User candidate) throws UserServiceException;
    User findById(Long id) throws UserServiceException;
    User findByUsername(String username) throws UserServiceException;
    List<User> findAll();
    void edit(User target, User source) throws UserServiceException;
    void update(User user);
    void delete(Long id);

}
