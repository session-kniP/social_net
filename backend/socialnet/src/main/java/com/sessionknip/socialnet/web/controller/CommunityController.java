package com.sessionknip.socialnet.web.controller;

import com.sessionknip.socialnet.web.domain.User;
import com.sessionknip.socialnet.web.domain.UserInfo;
import com.sessionknip.socialnet.web.dto.MessageDto;
import com.sessionknip.socialnet.web.dto.UserDto;
import com.sessionknip.socialnet.web.dto.request.UserCommunityRequestDto;
import com.sessionknip.socialnet.web.dto.response.UserCommunityResponseDto;
import com.sessionknip.socialnet.web.security.UserDetailsImpl;
import com.sessionknip.socialnet.web.service.UserCommunityService;
import com.sessionknip.socialnet.web.service.UserInfoService;
import com.sessionknip.socialnet.web.service.UserService;
import com.sessionknip.socialnet.web.service.exception.CommunityException;
import com.sessionknip.socialnet.web.service.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/community")
public class CommunityController {

    private final UserCommunityService userCommunityService;
    private final UserService userService;
    private final UserInfoService userInfoService;

    public CommunityController(UserCommunityService userCommunityService, UserService userService, UserInfoService userInfoService) {
        this.userCommunityService = userCommunityService;
        this.userService = userService;
        this.userInfoService = userInfoService;
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam Long id) {

        try {
            User resultUser = userService.findById(id);
            UserDto resultDto = new UserDto(resultUser);
            resultDto.setCommunityStatus(userCommunityService.getCommunityStatus(userDetails.getUser(), resultUser));

            return new ResponseEntity<>(resultDto, HttpStatus.OK);
        } catch (UserException e) {
            return new ResponseEntity<>(new MessageDto("Can't find user with id " + id), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAll(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer howMuch,
            @RequestParam(required = false) String[] filters
    ) {
        //todo use filters
        if (page == null) {
            page = 0;
        }

        if (howMuch == null) {
            howMuch = 10;
        }

        List<UserInfo> userInfoList;
        List<UserDto> users;

        if (filters == null || filters.length == 0) {
            userInfoList = userInfoService.findSeveral(page, howMuch);
        } else {
            userInfoList = userInfoService.findByFilters(page, howMuch, filters);
        }

        users = userInfoList.stream().map(u -> new UserDto(u.getUser())).collect(Collectors.toList());

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/friends")
    public ResponseEntity<List<UserDto>> getFriends(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer howMuch
    ) {
//        List<UserDto> friends = userDetails.getUser().getUserCommunity().getUserFriends().stream().map(UserDto::new).collect(Collectors.toList());
        List<UserDto> friends = userCommunityService.getFriends(userDetails.getUser(), page, howMuch).stream().map(UserDto::new).collect(Collectors.toList());

        return new ResponseEntity<>(friends, HttpStatus.OK);
    }

    @GetMapping("/subscribers")
    public ResponseEntity<List<UserDto>> getSubs(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer howMuch
    ) {
//        List<UserDto> subs = userDetails.getUser().getUserCommunity().getSubscribers().stream().map(UserDto::new).collect(Collectors.toList());
        List<UserDto> subs = userCommunityService.getSubscribers(userDetails.getUser(), page, howMuch).stream().map(UserDto::new).collect(Collectors.toList());

        return new ResponseEntity<>(subs, HttpStatus.OK);
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<List<UserDto>> getSubscriptions(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer howMuch
    ) {
//        List<UserDto> subs = userDetails.getUser().getUserCommunity().getSubscriptions().stream().map(UserDto::new).collect(Collectors.toList());
        List<UserDto> subscriptions = userCommunityService.getSubscriptions(userDetails.getUser(), page, howMuch).stream().map(UserDto::new).collect(Collectors.toList());

        return new ResponseEntity<>(subscriptions, HttpStatus.OK);
    }

    @PostMapping("/subscribe")
    public ResponseEntity<MessageDto> subscribe(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody UserCommunityRequestDto request) {

        User candidate;

        try {
            candidate = userService.findById(request.getId());
        } catch (UserException e) {
            return new ResponseEntity<>(new MessageDto("This user doesn't exists"), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        try {
            userCommunityService.addSubscription(userDetails.getUser(), candidate);
        } catch (CommunityException e) {
            return new ResponseEntity<>(new MessageDto(e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(new MessageDto("You successfully subscribed to this user"), HttpStatus.OK);
    }


    @PostMapping("/unsubscribe")
    public ResponseEntity<MessageDto> unsubscribe(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody UserCommunityRequestDto request) {

        User candidate;

        try {
            candidate = userService.findById(request.getId());
        } catch (UserException e) {
            return new ResponseEntity<>(new MessageDto("This user doesn't exists"), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        try {
            userCommunityService.removeSubscription(userDetails.getUser(), candidate);
        } catch (CommunityException e) {
            return new ResponseEntity<>(new MessageDto(e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(new MessageDto(String.format("You successfully unsubscribed from %s", candidate)), HttpStatus.OK);
    }


    @PostMapping("/acceptFriendRequest")
    public ResponseEntity<MessageDto> acceptFriendRequest(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody UserCommunityRequestDto requestDto
    ) {
        User candidate;

        try {
            candidate = userService.findById(requestDto.getId());
        } catch (UserException e) {
            return new ResponseEntity<>(new MessageDto(e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        try {
            userCommunityService.addFriend(userDetails.getUser(), candidate);
        } catch (CommunityException e) {
            return new ResponseEntity<>(new MessageDto(e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(new MessageDto(String.format("%s is now your friend", candidate)), HttpStatus.OK);
    }


    @PostMapping("/removeFriend")
    public ResponseEntity<MessageDto> removeFriend(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody UserCommunityRequestDto requestDto) {

        User candidate;

        try {
            candidate = userService.findById(requestDto.getId());
        } catch (UserException e) {
            return new ResponseEntity<>(new MessageDto(e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        try {
            userCommunityService.removeFriend(userDetails.getUser(), candidate);
        } catch (CommunityException e) {
            return new ResponseEntity<>(new MessageDto(e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(
                new MessageDto(
                        String.format("You successfully removed %s from your friends list", candidate.getUsername())),
                HttpStatus.OK);
    }

}
