package com.sessionknip.socialnet.web.config;

import com.sessionknip.socialnet.web.security.TokenFilter;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class SecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final TokenFilter filter;


    public SecurityConfig(TokenFilter filter) {
        this.filter = filter;
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        builder.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
