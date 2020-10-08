package com.sessionknip.socialnet.web.config;

import com.sessionknip.socialnet.web.domain.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import javax.annotation.PostConstruct;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecurityConfig config;

    private final String ADMIN_ENDPOINT;
    private final String AUTH_ENDPOINT;

    public WebSecurityConfig(SecurityConfig config, @Value("${api.version}") String api) {
        this.config = config;
        this.ADMIN_ENDPOINT = String.format("/api/%s/admin/**", api);
        this.AUTH_ENDPOINT = String.format("/api/%s/auth/**", api);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .disable()
                .csrf().disable()
//                    .csrfTokenRepository(csrfTokenRepository())  //CookieCsrfTokenRepository.withHttpOnlyFalse()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers()
                .frameOptions().sameOrigin()
                .and()
                .authorizeRequests()
                .antMatchers("/", AUTH_ENDPOINT, "/ws/**").permitAll()
                .antMatchers(ADMIN_ENDPOINT).hasRole(Role.ADMIN.name())
                .anyRequest().authenticated()
                .and()
                .apply(config)
                .and()
                .cors();

    }

}
