package com.sessionknip.socialnet.web.controller;

import com.sessionknip.socialnet.web.domain.Publication;
import com.sessionknip.socialnet.web.dto.MessageDto;
import com.sessionknip.socialnet.web.dto.request.NewsRequestDto;
import com.sessionknip.socialnet.web.dto.request.PublicationRequestDto;
import com.sessionknip.socialnet.web.dto.response.PublicationResponseDto;
import com.sessionknip.socialnet.web.security.UserDetailsImpl;
import com.sessionknip.socialnet.web.service.NullAndEmptyChecker;
import com.sessionknip.socialnet.web.service.exception.PublicationException;
import com.sessionknip.socialnet.web.service.impl.PublicationServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/api/publications")
public class PublicationController extends NullAndEmptyChecker {

    private final PublicationServiceImpl publicationService;

    public PublicationController(PublicationServiceImpl publicationService) {
        this.publicationService = publicationService;
    }

    @GetMapping("/news")
    public ResponseEntity<List<PublicationResponseDto>> getNews(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "howMuch", required = false) Integer howMuch
    ) {

        List<PublicationResponseDto> news;

        //page or howMuch can be null separately
        page = page == null ? 0 : page;
        howMuch = howMuch == null ? 10 : howMuch;

        news = publicationService.findMultiple(page, howMuch).stream().map(PublicationResponseDto::new).collect(Collectors.toList());

        return new ResponseEntity<>(news, HttpStatus.OK);
    }


    @PostMapping("/makePublication")
    public ResponseEntity<MessageDto> makePublication(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody PublicationRequestDto publicationDto
    ) {
        if (!notNullAndNotEmpty(publicationDto.getTheme()) || !notNullAndNotEmpty(publicationDto.getText())) {
            return new ResponseEntity<>(new MessageDto("All fields should be filled"), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Publication publication = new Publication(publicationDto.getTheme(), publicationDto.getText(), userDetails.getUser());

        try {
            publicationService.makePublication(publication);
        } catch (PublicationException e) {
            return new ResponseEntity<>(
                    new MessageDto("Can't make publication. Please, check entered values to see if they match the format"),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(new MessageDto("Your publication successfully posted"), HttpStatus.OK);
    }

}
