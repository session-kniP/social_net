package com.sessionknip.socialnet.web.service;

import com.sessionknip.socialnet.web.domain.Publication;
import com.sessionknip.socialnet.web.domain.User;
import com.sessionknip.socialnet.web.service.exception.PublicationServiceException;

import java.util.List;

public interface PublicationService {

    void makePublication(Publication publication) throws PublicationServiceException;
    Publication findById(Long id) throws PublicationServiceException;
    List<Publication> findByTheme(String theme) throws PublicationServiceException;
    List<Publication> findByKeywords(String[] keywords);
    List<Publication> findByKeywords(String[] keywords, Integer page, Integer howMuch);
    List<Publication> findMultiple();
    List<Publication> findMultiple(Integer page, Integer howMuch);
    List<Publication> findUserPublications(Integer page, Integer howMuch, User user);
    void edit(Publication target, Publication source);
    void remove(Publication publication);


}
