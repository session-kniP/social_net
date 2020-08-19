package com.sessionknip.socialnet.web.controller;

import com.sessionknip.socialnet.web.domain.Media;
import com.sessionknip.socialnet.web.domain.User;
import com.sessionknip.socialnet.web.domain.UserInfo;
import com.sessionknip.socialnet.web.dto.MessageDto;
import com.sessionknip.socialnet.web.dto.UserDto;
import com.sessionknip.socialnet.web.security.UserDetailsImpl;
import com.sessionknip.socialnet.web.service.MediaService;
import com.sessionknip.socialnet.web.service.UserService;
import com.sessionknip.socialnet.web.service.exception.MediaServiceException;
import com.sessionknip.socialnet.web.service.exception.UserException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;

    private final MediaService mediaService;

    @Value("${upload.files.path}")
    private String uploadPath;

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
        } catch (MediaServiceException | UserException e) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

    }

    @PostMapping("/edit")
    public ResponseEntity<MessageDto> edit(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody UserDto userDto) {

        User editUser = new User();
        //todo edit info nullable
        UserInfo editInfo = new UserInfo();

        editInfo.setFirstName(userDto.getUserInfo().getFirstName());
        editInfo.setLastName(userDto.getUserInfo().getLastName());
        editInfo.setSex(userDto.getUserInfo().getSex());
        editInfo.setEmail(userDto.getUserInfo().getEmail());

        editUser.setUserInfo(editInfo);

        try {
            userService.edit(userDetails.getUser(), editUser);
        } catch (UserException e) {
            return new ResponseEntity<>(new MessageDto(e.getMessage() + ". " + e.getCause().getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(new MessageDto("Profile successfully edited"), HttpStatus.OK);
    }

    @PostMapping("/loadAvatar")
    public ResponseEntity<MessageDto> loadAvatar(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam MultipartFile avatar
    ) {

        if (avatar != null) {

            String randomUUID = UUID.randomUUID().toString();
            String filename = randomUUID + "." + avatar.getOriginalFilename();

            try {
                mediaService.saveAvatar(userDetails.getUser(), new Media(filename), avatar);
            } catch (MediaServiceException e) {
                return new ResponseEntity<>(new MessageDto("Error while avatar uploading: " + e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }

        return new ResponseEntity<>(new MessageDto("Successfully uploaded"), HttpStatus.OK);
    }


}
