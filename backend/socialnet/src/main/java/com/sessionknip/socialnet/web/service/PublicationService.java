package com.sessionknip.socialnet.web.service;

import com.sessionknip.socialnet.web.domain.Publication;
import com.sessionknip.socialnet.web.service.exception.PublicationException;

import java.util.List;

public interface PublicationService {

    void makePublication(Publication publication) throws PublicationException;
    Publication findById(Long id) throws PublicationException;
    List<Publication> findByTheme(String theme) throws PublicationException;
    List<Publication> findByKeywords(String[] keywords);
    List<Publication> findByKeywords(String[] keywords, Integer page, Integer howMuch);
    List<Publication> findMultiple();
    List<Publication> findMultiple(Integer page, Integer howMuch);
    void edit(Publication target, Publication source);
    void remove(Publication publication);


}
