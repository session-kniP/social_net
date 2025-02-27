package com.sessionknip.socialnet.web.security;

import com.sessionknip.socialnet.web.domain.User;
import com.sessionknip.socialnet.web.service.exception.UserServiceException;
import com.sessionknip.socialnet.web.service.impl.UserServiceImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserServiceImpl userService;

    public UserDetailsServiceImpl(UserServiceImpl userService) {
        this.userService = userService;
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("Can't find user with username %s", username));
        }

        System.out.println("Loaded");
        return new UserDetailsImpl(user);
    }
}
