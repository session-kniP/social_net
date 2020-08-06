package com.sessionknip.socialnet.web.service.impl;

import com.sessionknip.socialnet.web.domain.Publication;
import com.sessionknip.socialnet.web.repository.PublicationRepo;
import com.sessionknip.socialnet.web.service.NullAndEmptyChecker;
import com.sessionknip.socialnet.web.service.PublicationService;
import com.sessionknip.socialnet.web.service.exception.PublicationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PublicationServiceImpl extends NullAndEmptyChecker implements PublicationService {

    private final PublicationRepo publicationRepo;

    public PublicationServiceImpl(PublicationRepo publicationRepo) {
        this.publicationRepo = publicationRepo;
    }

    @Override
    public void makePublication(Publication publication) throws PublicationException {
        //todo check format
        publicationRepo.save(publication);
    }

    @Override
    public Publication findById(Long id) throws PublicationException {
        return publicationRepo.findById(id).orElseThrow(() -> new PublicationException("Can't find publication by id"));
    }

    @Override
    public List<Publication> findByTheme(String theme) throws PublicationException {
        List<Publication> publications = publicationRepo.findByTheme(theme);
        if (publications == null || publications.size() == 0) {
            throw new PublicationException(String.format("Can't find publication with theme '%s'", theme));
        }

        return publications;
    }

    /**
     * @return first 10 elements matching keywords
     */
    @Override
    public List<Publication> findByKeywords(String[] keywords) {
        Set<Publication> publications = new HashSet<>();

        for (String kw : keywords) {
            List<Publication> result = publicationRepo.findByThemeLikeOrTextLikeIgnoreCaseOrderByPublicationDateDescPublicationTimeDesc(
                    String.format("%%%s%%", kw),
                    String.format("%%%s%%", kw),
                    PageRequest.of(0, 10)
            );
            publications.addAll(result);
        }

        return new ArrayList<>(publications);
    }

    @Override
    public List<Publication> findByKeywords(String[] keywords, Integer page, Integer howMuch) {
        Set<Publication> publications = new HashSet<>();

        for (String kw : keywords) {
            List<Publication> result = publicationRepo.findByThemeLikeOrTextLikeIgnoreCaseOrderByPublicationDateDescPublicationTimeDesc(
                    String.format("%%%s%%", kw),
                    String.format("%%%s%%", kw),
                    PageRequest.of(page, howMuch)
            );

            publications.addAll(result);
        }

        return new ArrayList<>(publications);
    }

    /**
     * @return first 10 last by date and time news
     */
    @Override
    public List<Publication> findMultiple() {
        return publicationRepo.findAllByOrderByPublicationDateDescPublicationTimeDesc(PageRequest.of(0, 10)).getContent();
    }

    @Override
    public List<Publication> findMultiple(Integer page, Integer howMuch) {
        return publicationRepo.findAllByOrderByPublicationDateDescPublicationTimeDesc(PageRequest.of(page, howMuch)).getContent();
    }

    @Override
    public void edit(Publication target, Publication source) {

        if (notNullAndNotEmpty(source.getTheme())) {
            target.setTheme(source.getTheme());
        }

        if (notNullAndNotEmpty(source.getText())) {
            target.setText(source.getText());
        }

        publicationRepo.save(target);
    }

    @Override
    public void remove(Publication publication) {
        publicationRepo.delete(publication);
    }
}
