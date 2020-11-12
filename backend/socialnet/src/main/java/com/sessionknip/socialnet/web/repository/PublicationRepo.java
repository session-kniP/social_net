package com.sessionknip.socialnet.web.repository;

import com.sessionknip.socialnet.web.domain.Publication;
import com.sessionknip.socialnet.web.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicationRepo extends JpaRepository<Publication, Long> {
    List<Publication> findByTheme(String theme);

    //    List<Publication> findAllBetween(Long first, Long second);
    List<Publication> findByThemeLikeOrTextLikeIgnoreCaseOrderByPublicationDateDescPublicationTimeDesc(String themeLike, String textLike, Pageable of);
    Page<Publication> findAllByOrderByPublicationDateDescPublicationTimeDesc(Pageable pages);

    @Query(value = "from Publication p where p.author=:author order by p.publicationDate desc, p.publicationTime desc")
    Page<Publication> findUsersPublications(@Param(value = "author") User author, Pageable pages);
}
