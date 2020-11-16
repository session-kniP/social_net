package com.sessionknip.socialnet.web.controller;

import com.sessionknip.socialnet.web.domain.Publication;
import com.sessionknip.socialnet.web.domain.User;
import com.sessionknip.socialnet.web.dto.InfoMessageDto;
import com.sessionknip.socialnet.web.dto.request.PublicationRequestDto;
import com.sessionknip.socialnet.web.dto.response.PublicationResponseDto;
import com.sessionknip.socialnet.web.security.UserDetailsImpl;
import com.sessionknip.socialnet.web.service.NullAndEmptyChecker;
import com.sessionknip.socialnet.web.service.PublicationService;
import com.sessionknip.socialnet.web.service.UserService;
import com.sessionknip.socialnet.web.service.exception.PublicationServiceException;
import com.sessionknip.socialnet.web.service.exception.UserServiceException;
import com.sessionknip.socialnet.web.service.impl.PublicationServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/api/v1/publications")
public class PublicationsController extends NullAndEmptyChecker {

    private final PublicationService publicationService;
    private final UserService userService;

    public PublicationsController(
            @Qualifier("publicationServiceImpl") PublicationService publicationService,
            @Qualifier("userServiceImpl") UserService userService
    ) {
        this.publicationService = publicationService;
        this.userService = userService;
    }

    @GetMapping("/news")
    public ResponseEntity<List<PublicationResponseDto>> getNews(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "howMuch", required = false) Integer howMuch,
            @RequestParam(name = "filters", required = false) String[] filters
    ) {
        //todo user filters
        List<PublicationResponseDto> news;

        //page or howMuch can be null separately
        page = page == null ? 0 : page;
        howMuch = howMuch == null ? 10 : howMuch;

        news = publicationService.findMultiple(page, howMuch).stream().map(PublicationResponseDto::new).collect(Collectors.toList());

        return new ResponseEntity<>(news, HttpStatus.OK);
    }

    @GetMapping("/userNews")
    public ResponseEntity<?> getUserNews(
            @RequestParam(name = "userId", required = false) Long userId,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "howMuch", required = false) Integer howMuch,
            @RequestParam(name = "filters", required = false) String[] filters,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        page = page == null ? 0 : page;
        howMuch = howMuch == null ? 10 : howMuch;

        List<PublicationResponseDto> news;

        if (userId == null) {
            news = publicationService.findUserPublications(page, howMuch, userDetails.getUser())
                    .stream().map(PublicationResponseDto::new).collect(Collectors.toList());
        } else {
            User user = userService.findById(userId);
            if (user == null) {
                return new ResponseEntity<>(new InfoMessageDto("Can't find user with id " + userId), HttpStatus.UNPROCESSABLE_ENTITY);
            }

            news = publicationService.findUserPublications(page, howMuch, user)
                    .stream().map(PublicationResponseDto::new).collect(Collectors.toList());

        }

        return new ResponseEntity<>(news, HttpStatus.OK);
    }

    @PostMapping("/makePublication")
    public ResponseEntity<InfoMessageDto> makePublication(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody PublicationRequestDto publicationDto
    ) {
        if (!notNullAndNotEmpty(publicationDto.getTheme()) || !notNullAndNotEmpty(publicationDto.getText())) {
            return new ResponseEntity<>(new InfoMessageDto("All fields should be filled"), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Publication publication = new Publication(publicationDto.getTheme(), publicationDto.getText(), userDetails.getUser());

        try {
            publicationService.makePublication(publication);
        } catch (PublicationServiceException e) {
            return new ResponseEntity<>(
                    new InfoMessageDto("Can't make publication. Please, check entered values to see if they match the format"),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(new InfoMessageDto("Your publication successfully posted"), HttpStatus.OK);
    }

}
