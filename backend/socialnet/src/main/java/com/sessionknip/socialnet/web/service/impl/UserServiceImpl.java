package com.sessionknip.socialnet.web.service.impl;

import com.sessionknip.socialnet.web.domain.User;
import com.sessionknip.socialnet.web.repository.UserRepo;
import com.sessionknip.socialnet.web.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Bean
    private BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    @Override
    public User register(User candidate) {
        User user = userRepo.findByUsername(candidate.getUsername());

        if (user != null) {
            return null;
        }

        user = new User(candidate.getUsername(), passwordEncoder().encode(candidate.getPassword()));
        userRepo.save(user);

        return user;
    }

    @Override
    public User findById(Long id) {
        User user = userRepo.findById(id).orElse(null);
        //todo log + exception
        return user;
    }

    @Override
    public User findByUsername(String username) {
        User user = userRepo.findByUsername(username);

        return user;
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public void delete(Long id) {
        userRepo.deleteById(id);
    }
}
