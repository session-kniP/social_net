package com.sessionknip.socialnet.web.service.impl;

import com.sessionknip.socialnet.web.domain.Media;
import com.sessionknip.socialnet.web.domain.User;
import com.sessionknip.socialnet.web.repository.MediaRepo;
import com.sessionknip.socialnet.web.service.MediaService;
import com.sessionknip.socialnet.web.service.UserService;
import com.sessionknip.socialnet.web.service.exception.MediaServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class MediaServiceImpl implements MediaService {

    @Value("${upload.files.path}")
    private String uploadPath;

    private final MediaRepo mediaRepo;

    private final UserService userService;

    public MediaServiceImpl(MediaRepo mediaRepo, UserService userService) {
        this.mediaRepo = mediaRepo;
        this.userService = userService;
    }

    @Override
    public void saveAvatar(User user, Media avatarInfo, MultipartFile avatar) throws MediaServiceException {

        String avatarPath = String.format("%s/%s/avatar", uploadPath, user.getUsername());
        File uploadDir = new File(avatarPath);

        if (!uploadDir.exists()) {
            if (!uploadDir.mkdirs()) {
                throw new MediaServiceException("Error while avatar uploading");
            }
        }

        try {
            avatar.transferTo(new File(avatarPath + "/" + avatarInfo.getMediaName()));
        } catch (IOException e) {
            throw new MediaServiceException("Can't save avatar", e);
        }
        user.getUserInfo().setAvatar(avatarInfo);
        mediaRepo.save(avatarInfo);

        userService.update(user);
    }

    @Override
    public Resource getAvatar(User user) throws MediaServiceException {
        try {
            String avatarPath = String.format("%s/%s/avatar", uploadPath, user.getUsername());
            return new FileSystemResource(new File(avatarPath + "/" + user.getUserInfo().getAvatar().getMediaName()));
        } catch (NullPointerException e) {
            throw new MediaServiceException("Avatar is null", e);
        }
    }
}
