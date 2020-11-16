package com.sessionknip.socialnet.web.controller;

import com.sessionknip.socialnet.web.domain.User;
import com.sessionknip.socialnet.web.dto.request.LoginRequestDto;
import com.sessionknip.socialnet.web.dto.request.RegRequestDto;
import com.sessionknip.socialnet.web.dto.response.LoginResponseDto;
import com.sessionknip.socialnet.web.security.TokenProvider;
import com.sessionknip.socialnet.web.service.exception.UserServiceException;
import com.sessionknip.socialnet.web.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@Controller
@RequestMapping(value = "/api/v1/auth")
@RestController
public class AuthController {

    private final UserServiceImpl userService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider provider;
    private final BCryptPasswordEncoder encoder;


    public AuthController(
            @Qualifier("userServiceImpl") UserServiceImpl userService,
            AuthenticationManager authenticationManager,
            TokenProvider provider,
            BCryptPasswordEncoder encoder
    ) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.provider = provider;
        this.encoder = encoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        User user = userService.findByUsername(username);
        if (user == null) {
            return new ResponseEntity<>(new LoginResponseDto("Incorrect username or password"), HttpStatus.FORBIDDEN);
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new LoginResponseDto("Incorrect username or password"), HttpStatus.FORBIDDEN);
        }


        String token = provider.createToken(username, new ArrayList<>(user.getRoles()));

        LoginResponseDto response = new LoginResponseDto(user.getId(), username, token);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegRequestDto regRequest) {
        User candidate = userService.findByUsername(regRequest.getUsername());

        if (candidate != null) {
            return new ResponseEntity<>("User with such username already exists", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (!regRequest.getPassword().equals(regRequest.getRepeatPassword())) {
            return new ResponseEntity<>("Passwords don't match", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        try {
            userService.register(new User(regRequest.getUsername(), regRequest.getPassword()));
        } catch (UserServiceException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>("You successfully registered", HttpStatus.OK);
    }


}
