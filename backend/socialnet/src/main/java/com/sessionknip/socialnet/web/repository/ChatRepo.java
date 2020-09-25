package com.sessionknip.socialnet.web.repository;

import com.sessionknip.socialnet.web.domain.Chat;
import com.sessionknip.socialnet.web.domain.ChatType;
import com.sessionknip.socialnet.web.domain.UserCommunity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRepo extends JpaRepository<Chat, Long> {

    @Query("from Chat")
    Optional<Chat> findById(Long id);

    @Query("from Chat c where :userCommunity member of c.members")
    Page<Chat> findByUserCommunity(@Param("userCommunity")UserCommunity userCommunity, Pageable pageable);

    //todo optimize: remove chat type, because it's private, or replace method by smth like 'findByTwoUserCommunities()'
    @Query("from Chat c where :first member of c.members and :second member of c.members and c.type = :type")
    List<Chat> findPrivate(@Param("first") UserCommunity first, @Param("second") UserCommunity second, @Param("type") ChatType type, Pageable pageable);

}
