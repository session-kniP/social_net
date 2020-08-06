package com.sessionknip.socialnet.web.repository;

import com.sessionknip.socialnet.web.domain.Publication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicationRepo extends JpaRepository<Publication, Long> {
    List<Publication> findByTheme(String theme);

    //    List<Publication> findAllBetween(Long first, Long second);
    List<Publication> findByThemeLikeOrTextLikeIgnoreCaseOrderByPublicationDateDescPublicationTimeDesc(String themeLike, String textLike, Pageable of);
    Page<Publication> findAllByOrderByPublicationDateDescPublicationTimeDesc(Pageable pages);
}
