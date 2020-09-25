package com.sessionknip.socialnet.web.security;

import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;

//custom principal for preSend of stomp event filter
public class WebSocketPrincipal implements Principal {

    private final UserDetailsImpl userDetails;

    public WebSocketPrincipal(UserDetailsImpl userDetails) {
        this.userDetails = userDetails;
    }

    @Override
    public String getName() {
        return userDetails.getUsername();
    }

    public UserDetailsImpl getUserDetails() {
        return userDetails;
    }
}
