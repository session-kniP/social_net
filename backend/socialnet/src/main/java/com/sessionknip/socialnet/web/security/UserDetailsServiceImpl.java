package com.sessionknip.socialnet.web.security;

import com.sessionknip.socialnet.web.domain.User;
import com.sessionknip.socialnet.web.service.impl.UserServiceImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserServiceImpl userService;

    public UserDetailsServiceImpl(UserServiceImpl userService) {
        this.userService = userService;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("Can't find user with username %s", username));
        }

        return new UserDetailsImpl(user);
    }
}
