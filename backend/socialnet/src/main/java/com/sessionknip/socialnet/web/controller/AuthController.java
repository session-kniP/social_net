package com.sessionknip.socialnet.web.controller;

import com.sessionknip.socialnet.web.domain.Role;
import com.sessionknip.socialnet.web.domain.User;
import com.sessionknip.socialnet.web.dto.LoginRequestDto;
import com.sessionknip.socialnet.web.dto.LoginResponseDto;
import com.sessionknip.socialnet.web.dto.RegRequestDto;
import com.sessionknip.socialnet.web.security.TokenProvider;
import com.sessionknip.socialnet.web.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/api/auth")
public class AuthController {

    private final UserServiceImpl userService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider provider;


    public AuthController(UserServiceImpl userService, AuthenticationManager authenticationManager, TokenProvider provider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.provider = provider;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        User user = userService.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("Can't find user with username %s", username));
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        String token = provider.createToken(username, new ArrayList<>(user.getRoles()));

        LoginResponseDto response = new LoginResponseDto(username, token);

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

        userService.register(new User(regRequest.getUsername(), regRequest.getPassword()));

        return new ResponseEntity<>("You successfully registered", HttpStatus.OK);
    }

    @GetMapping("/auth_success")
    @ResponseBody
    public String authSuccess() {
        System.out.println("Auth success");
//        AuthenticationManager manager = new AuthenticationMa(userService);
        return "Auth success churka nahoi";
    }

    @PostMapping("/auth_fail")
    @ResponseBody
    public String authFail() {
        System.out.println("Auth fail");
        return null;
    }


}
