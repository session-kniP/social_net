package com.sessionknip.socialnet.web.service;

import com.sessionknip.socialnet.web.domain.User;
import com.sessionknip.socialnet.web.service.exception.UserException;

import java.util.List;

public interface UserService {

    User register(User candidate) throws UserException;
    User findById(Long id) throws UserException;
    User findByUsername(String username) throws UserException;
    List<User> findAll();
    void edit(User target, User source) throws UserException;
    void update(User user);
    void delete(Long id);

}
