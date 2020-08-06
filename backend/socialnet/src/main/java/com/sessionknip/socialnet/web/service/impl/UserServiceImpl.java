package com.sessionknip.socialnet.web.service.impl;

import com.sessionknip.socialnet.web.domain.User;
import com.sessionknip.socialnet.web.domain.UserInfo;
import com.sessionknip.socialnet.web.repository.UserRepo;
import com.sessionknip.socialnet.web.service.NullAndEmptyChecker;
import com.sessionknip.socialnet.web.service.UserInfoService;
import com.sessionknip.socialnet.web.service.UserService;
import com.sessionknip.socialnet.web.service.exception.UserException;
import com.sessionknip.socialnet.web.service.exception.UserInfoException;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl extends NullAndEmptyChecker implements UserService {

    private final UserRepo userRepo;
    private final UserInfoService userInfoService;

    public UserServiceImpl(UserRepo userRepo, UserInfoService userInfoService) {
        this.userRepo = userRepo;
        this.userInfoService = userInfoService;
    }

    @Bean
    private BCryptPasswordEncoder passwordEncoder() throws UserException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    @Override
    public User register(User candidate) throws UserException {
        User user = userRepo.findByUsername(candidate.getUsername());

        if (user != null) {
            throw new UserException("User with such username already exists");
        }

        user = new User(candidate.getUsername(), passwordEncoder().encode(candidate.getPassword()));
        userRepo.save(user);

        return user;
    }

    @Override
    public User findById(Long id) throws UserException {
        return userRepo.findById(id).orElseThrow(() -> new UserException("Can't find user by id"));
        //todo log + exception
    }

    @Override
    public User findByUsername(String username) throws UserException {
        User user = userRepo.findByUsername(username);

        if (user == null) {
            throw new UserException(String.format("Can't find user with username '%s'", username));
        }

        return user;
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    @Transactional
    public void edit(User target, User source) throws UserException {
        try {
            target.getUserInfo().setUser(target);
            UserInfo userInfo = userInfoService.editNotSave(target.getUserInfo(), source.getUserInfo());

            target.setUserInfo(userInfo);
        } catch (UserInfoException e) {
            throw new UserException("Can't edit user info", e);
        }

        userRepo.save(target);
    }

    @Override
    public void delete(Long id) {
        userRepo.deleteById(id);
    }

}
