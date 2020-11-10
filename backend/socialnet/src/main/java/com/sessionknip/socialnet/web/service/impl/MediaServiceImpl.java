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

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MediaServiceImpl implements MediaService {

    @Value("${upload.files.avatars.users.path}")
    private String uploadPath;

    private final MediaRepo mediaRepo;

    private final UserService userService;

    public MediaServiceImpl(MediaRepo mediaRepo, UserService userService) {
        this.mediaRepo = mediaRepo;
        this.userService = userService;
    }

    @Override
    @Transactional
    public void saveAvatar(User user, MultipartFile avatar) throws MediaServiceException {

        String randomUUID = UUID.randomUUID().toString();
        String filename = randomUUID + "." + avatar.getOriginalFilename();

        Media avatarInfo = new Media(filename);

        String avatarPath = String.format("%s/%s/avatar", uploadPath, user.getId());
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
            String avatarPath = String.format("%s/%s/avatar", uploadPath, user.getId());
            File dir = new File(avatarPath + "/" + user.getUserInfo().getAvatar().getMediaName());

            if (!dir.exists()) {
                return null;
            }

            return new FileSystemResource(dir);
        } catch (NullPointerException e) {
            throw new MediaServiceException("Avatar is null", e);
        }
    }

    @Override
    @Transactional
    public void deleteAvatar(User user) throws MediaServiceException {
        String avatarPath = String.format("%s/%s/avatar", uploadPath, user.getId());
        File avatar = new File(avatarPath + "/" + user.getUserInfo().getAvatar().getMediaName());

        if (!avatar.exists()) {
            throw new MediaServiceException("Avatar doesn't exists");
        }

        if (!avatar.delete()) {
            throw new MediaServiceException("Can't delete avatar");
        }

        File avatarDir = new File(avatarPath);

        if (!avatarDir.exists()) {
            throw new MediaServiceException("Avatar doesn't exists");
        }

        List<File> attributes = Arrays.stream(Objects.requireNonNull(avatarDir.listFiles()))
                .sorted((prev, next) -> {
                    assert prev != null;
                    try {
                        return Files.readAttributes(next.toPath(), BasicFileAttributes.class).creationTime()
                                .compareTo(Files.readAttributes(prev.toPath(), BasicFileAttributes.class).creationTime());
                    } catch (IOException e) {
                        return 0;
                    }
                })
                .collect(Collectors.toList());

        Media avatarInfo = new Media(attributes.get(0).getName());
        user.getUserInfo().setAvatar(avatarInfo);
        mediaRepo.save(avatarInfo);

        userService.update(user);
    }
}
