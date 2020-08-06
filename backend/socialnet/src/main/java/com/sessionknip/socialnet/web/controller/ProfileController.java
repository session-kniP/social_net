package com.sessionknip.socialnet.web.controller;

import com.sessionknip.socialnet.web.domain.User;
import com.sessionknip.socialnet.web.domain.UserInfo;
import com.sessionknip.socialnet.web.dto.UserDto;
import com.sessionknip.socialnet.web.security.UserDetailsImpl;
import com.sessionknip.socialnet.web.service.UserService;
import com.sessionknip.socialnet.web.service.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;


    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<UserDto> profile(@AuthenticationPrincipal UserDetailsImpl user) {

        System.out.println(user.getUsername() + " сука " + user.getAuthorities());
        UserDto dto = new UserDto(user.getUser());

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/edit")
    public ResponseEntity<String> edit(@AuthenticationPrincipal UserDetailsImpl user, @RequestBody UserDto userDto) {

        User editUser = new User();
        //todo edit info nullable
        UserInfo editInfo = new UserInfo();

        editInfo.setFirstName(userDto.getUserInfo().getFirstName());
        editInfo.setLastName(userDto.getUserInfo().getLastName());
        editInfo.setSex(userDto.getUserInfo().getSex());
        editInfo.setEmail(userDto.getUserInfo().getEmail());

        editUser.setUserInfo(editInfo);

        try {
            userService.edit(user.getUser(), editUser);
        } catch (UserException e) {
            return new ResponseEntity<>(e.getMessage() + ". " + e.getCause().getMessage() ,HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>("Profile successfully edited", HttpStatus.OK);
    }


}
