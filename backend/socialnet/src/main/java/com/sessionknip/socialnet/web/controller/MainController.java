package com.sessionknip.socialnet.web.controller;

import com.sessionknip.socialnet.web.dto.InfoMessageDto;
import com.sessionknip.socialnet.web.security.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("/api/v1")
public class MainController {

    @GetMapping("/")
    public ResponseEntity<InfoMessageDto> test(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails != null) {
            return new ResponseEntity<>(new InfoMessageDto(String.format(
                    "Hello, %s %s",
                    userDetails.getUser().getUserInfo().getFirstName(),
                    userDetails.getUser().getUserInfo().getLastName())), HttpStatus.OK);
        }
        return new ResponseEntity<>(new InfoMessageDto("Hello, mate!!!"), HttpStatus.OK);
    }

}
