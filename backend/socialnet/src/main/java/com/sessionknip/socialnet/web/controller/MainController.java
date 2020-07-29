package com.sessionknip.socialnet.web.controller;

import com.sessionknip.socialnet.web.domain.User;
import com.sessionknip.socialnet.web.dto.UserDto;
import com.sessionknip.socialnet.web.service.UserService;
import com.sessionknip.socialnet.web.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MainController {

    private final UserServiceImpl userService;

    public MainController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String test() {
        return null;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> users() {

        List<User> allUsers = userService.findAll();
        List<UserDto> dto = allUsers.stream().map(UserDto::new).collect(Collectors.toList());
        dto.forEach(System.out::println);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

}
