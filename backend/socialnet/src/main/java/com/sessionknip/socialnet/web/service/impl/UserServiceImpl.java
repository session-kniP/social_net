package com.sessionknip.socialnet.web.service.impl;

import com.sessionknip.socialnet.web.domain.User;
import com.sessionknip.socialnet.web.domain.UserInfo;
import com.sessionknip.socialnet.web.repository.UserRepo;
import com.sessionknip.socialnet.web.service.NullAndEmptyChecker;
import com.sessionknip.socialnet.web.service.UserInfoService;
import com.sessionknip.socialnet.web.service.UserService;
import com.sessionknip.socialnet.web.service.exception.UserServiceException;
import com.sessionknip.socialnet.web.service.exception.UserInfoServiceException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("userServiceImpl")
public class UserServiceImpl extends NullAndEmptyChecker implements UserService {

    private final UserRepo userRepo;
    private final UserInfoService userInfoService;

    public UserServiceImpl(UserRepo userRepo, @Qualifier("userInfoServiceImpl") UserInfoService userInfoService) {
        this.userRepo = userRepo;
        this.userInfoService = userInfoService;
    }

    @Bean
    private BCryptPasswordEncoder passwordEncoder() throws UserServiceException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    @Override
    public User register(User candidate) throws UserServiceException {
        User user = userRepo.findByUsername(candidate.getUsername());

        if (user != null) {
            throw new UserServiceException("User with such username already exists");
        }

        user = new User(candidate.getUsername(), passwordEncoder().encode(candidate.getPassword()));
        userRepo.save(user);

        return user;
    }

    /**
     * @return User with such id or null if user not found
     * */
    @Override
    public User findById(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    /**
     * @return User with such username or null if user not found
     * */
    @Override
    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    @Transactional
    public void edit(User target, User source) throws UserServiceException {
        try {
            target.getUserInfo().setUser(target);
            UserInfo userInfo = userInfoService.editNotSave(target.getUserInfo(), source.getUserInfo());

            target.setUserInfo(userInfo);
        } catch (UserInfoServiceException e) {
            throw new UserServiceException("Can't edit user info", e);
        }

        userRepo.save(target);
    }

    @Override
    public void update(User user) {
        userRepo.save(user);
    }

    @Override
    public void delete(Long id) {
        userRepo.deleteById(id);
    }

}
