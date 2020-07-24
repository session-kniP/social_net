package com.sessionknip.socialnet.web.repository;

import com.sessionknip.socialnet.web.domain.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicationRepo extends JpaRepository<Publication, Long> {
    Publication findByTheme(String theme);
}
