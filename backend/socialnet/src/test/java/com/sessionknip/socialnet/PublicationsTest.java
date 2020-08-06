package com.sessionknip.socialnet;

import com.sessionknip.socialnet.web.domain.Publication;
import com.sessionknip.socialnet.web.domain.User;
import com.sessionknip.socialnet.web.domain.UserInfo;
import com.sessionknip.socialnet.web.repository.PublicationRepo;
import com.sessionknip.socialnet.web.service.exception.UserException;
import com.sessionknip.socialnet.web.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@SpringBootTest
@EnableJpaRepositories
public class PublicationsTest {

    @Autowired
    public PublicationRepo repo;

    @Autowired
    public UserServiceImpl userService;

    @Test
    public void findBetween() {
        List<Publication> allBetween = repo.findByThemeLikeOrTextLikeIgnoreCaseOrderByPublicationDateDescPublicationTimeDesc("%Sample%", "%Sample%", PageRequest.of(0, 10));

        assert allBetween.size() == 3;

    }

    @Test
    public void userInfoTest() {
        User user = new User("Lazy", "123");

        User registered = null;
        try {
            registered = userService.register(user);
        } catch (UserException e) {
            e.printStackTrace();
        }

        assert registered != null;
    }

}
