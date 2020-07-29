package com.sessionknip.socialnet.web.config;

import com.sessionknip.socialnet.web.security.TokenFilter;
import com.sessionknip.socialnet.web.security.TokenProvider;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class SecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final TokenProvider provider;


    public SecurityConfig(TokenProvider provider) {
        this.provider = provider;
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        TokenFilter filter = new TokenFilter(provider);
        builder.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
