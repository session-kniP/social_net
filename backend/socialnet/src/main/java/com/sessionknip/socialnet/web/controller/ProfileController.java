package com.sessionknip.socialnet.web.controller;

import com.sessionknip.socialnet.web.domain.User;
import com.sessionknip.socialnet.web.domain.UserInfo;
import com.sessionknip.socialnet.web.dto.InfoMessageDto;
import com.sessionknip.socialnet.web.dto.community.UserDto;
import com.sessionknip.socialnet.web.dto.community.UserInfoDto;
import com.sessionknip.socialnet.web.security.UserDetailsImpl;
import com.sessionknip.socialnet.web.service.MediaService;
import com.sessionknip.socialnet.web.service.UserService;
import com.sessionknip.socialnet.web.service.exception.MediaServiceException;
import com.sessionknip.socialnet.web.service.exception.UserServiceException;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/api/v1/profile")
public class ProfileController {

    private final UserService userService;

    private final MediaService mediaService;

    public ProfileController(UserService userService, MediaService mediaService) {
        this.userService = userService;
        this.mediaService = mediaService;
    }

    @GetMapping("")
    public ResponseEntity<UserDto> profile(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        UserDto dto = new UserDto(userDetails.getUser());

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping(value = "/getAvatar", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> getAvatar(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(required = false) Long id
    ) {

        try {
            User user;
            if (id != null) {
                user = userService.findById(id);
            } else {
                user = userDetails.getUser();
            }

            Resource avatar = mediaService.getAvatar(user);
            return new ResponseEntity<>(avatar, HttpStatus.OK);
        } catch (MediaServiceException | UserServiceException e) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

    }

    @PostMapping("/edit")
    public ResponseEntity<InfoMessageDto> edit(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody UserDto userDto) {

        User editUser = new User();

        //dto to entity
        ModelMapper mapper = new ModelMapper();
        UserInfo editInfo = mapper.map(userDto.getUserInfo(), UserInfo.class);

        editUser.setUserInfo(editInfo);

        try {
            userService.edit(userDetails.getUser(), editUser);
        } catch (UserServiceException e) {
            return new ResponseEntity<>(new InfoMessageDto(e.getMessage() + ". " + e.getCause().getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(new InfoMessageDto("Profile successfully edited"), HttpStatus.OK);
    }

    @PostMapping("/uploadAvatar")
    public ResponseEntity<InfoMessageDto> loadAvatar(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam MultipartFile avatar
    ) {

        if (avatar != null) {

            try {
                mediaService.saveAvatar(userDetails.getUser(), avatar);
            } catch (MediaServiceException e) {
                return new ResponseEntity<>(new InfoMessageDto("Error while avatar uploading: " + e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }

        return new ResponseEntity<>(new InfoMessageDto("Successfully uploaded"), HttpStatus.OK);
    }


}
