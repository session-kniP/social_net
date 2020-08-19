package com.sessionknip.socialnet.web.repository;

import com.sessionknip.socialnet.web.domain.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepo extends JpaRepository<Media, Long> {

}
