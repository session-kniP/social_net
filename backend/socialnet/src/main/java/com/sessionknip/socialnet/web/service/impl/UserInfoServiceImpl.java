package com.sessionknip.socialnet.web.service.impl;

import com.sessionknip.socialnet.web.domain.UserInfo;
import com.sessionknip.socialnet.web.repository.UserInfoRepo;
import com.sessionknip.socialnet.web.service.NullAndEmptyChecker;
import com.sessionknip.socialnet.web.service.UserInfoService;
import com.sessionknip.socialnet.web.service.exception.UserInfoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserInfoServiceImpl extends NullAndEmptyChecker implements UserInfoService {

    private final UserInfoRepo userInfoRepo;

    public UserInfoServiceImpl(UserInfoRepo userInfoRepo) {
        this.userInfoRepo = userInfoRepo;
    }

    @Override
    public List<UserInfo> findAll() {
        return userInfoRepo.findAll();
    }

    @Override
    public List<UserInfo> findByFirstName(String firstName) {
        return userInfoRepo.findByFirstName(firstName);
    }

    @Override
    public List<UserInfo> findByLastName(String lastName) {
        return userInfoRepo.findByLastName(lastName);
    }

    @Override
    public UserInfo findByEmail(String email) throws UserInfoException {
        UserInfo info = userInfoRepo.findByEmail(email);

        if (info == null) {
            throw new UserInfoException("No user info with such email exists");
        }

        return info;
    }

    @Override
    public boolean existsByEmail(String email) {
        try {
            return findByEmail(email) != null;
        } catch (UserInfoException e) {
            return false;
        }
    }

    @Override
    @Transactional
    public void edit(UserInfo target, UserInfo source) throws UserInfoException {
        target = editNotSave(target, source);

        userInfoRepo.save(target);
    }

    @Override
    public UserInfo editNotSave(UserInfo target, UserInfo source) throws UserInfoException {

        if (notNullAndNotEmpty(source.getFirstName())) {
            target.setFirstName(source.getFirstName());
        }

        if (notNullAndNotEmpty(source.getLastName())) {
            target.setLastName(source.getLastName());
        }

        if (source.getSex() != null) {
            target.setSex(source.getSex());
        }

        if (notNullAndNotEmpty(source.getEmail())) {
            if (existsByEmail(source.getEmail())) {
                throw new UserInfoException("User info with such email already exists");
            }
            target.setEmail(source.getEmail());
        }

        return target;
    }

    @Override
    public void delete(Long userInfoId) {
        userInfoRepo.deleteById(userInfoId);
    }
}
