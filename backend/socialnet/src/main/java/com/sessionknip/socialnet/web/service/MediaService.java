package com.sessionknip.socialnet.web.service;

import com.sessionknip.socialnet.web.domain.Media;
import com.sessionknip.socialnet.web.domain.User;
import com.sessionknip.socialnet.web.service.exception.MediaServiceException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface MediaService {

    void saveAvatar(User user, Media avatarInfo, MultipartFile avatar) throws MediaServiceException;
    Resource getAvatar(User user) throws MediaServiceException;

}
