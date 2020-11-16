package com.sessionknip.socialnet.web.service.impl;

import com.sessionknip.socialnet.web.domain.UserInfo;
import com.sessionknip.socialnet.web.repository.UserInfoRepo;
import com.sessionknip.socialnet.web.service.NullAndEmptyChecker;
import com.sessionknip.socialnet.web.service.UserInfoService;
import com.sessionknip.socialnet.web.service.exception.UserInfoServiceException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("userInfoServiceImpl")
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
    public UserInfo findByEmail(String email) throws UserInfoServiceException {
        UserInfo info = userInfoRepo.findByEmail(email);

        if (info == null) {
            throw new UserInfoServiceException("No user info with such email exists");
        }

        return info;
    }

    @Override
    public List<UserInfo> findByFilters(Integer page, Integer howMuch, String[] filters) {
        Set<UserInfo> users = new HashSet<>();

        for (String filter : filters) {
            String template = String.format("%s%%", filter.trim());
            users.addAll(userInfoRepo.findByFirstNameLikeOrLastNameLikeIgnoreCase(template, template, PageRequest.of(page, howMuch)));
        }

        return new ArrayList<>(users);
    }

    @Override
    public List<UserInfo> findSeveral(Integer page, Integer howMuch) {
        return userInfoRepo.findAll(PageRequest.of(page, howMuch)).getContent();
    }

    @Override
    public boolean existsByEmail(String email) {
        try {
            return findByEmail(email) != null;
        } catch (UserInfoServiceException e) {
            return false;
        }
    }

    @Override
    public void edit(UserInfo target, UserInfo source) throws UserInfoServiceException {
        target = editNotSave(target, source);

        userInfoRepo.save(target);
    }

    @Override
    public UserInfo editNotSave(UserInfo target, UserInfo source) throws UserInfoServiceException {

        if (notNullAndNotEmpty(source.getFirstName())) {
            target.setFirstName(source.getFirstName());
        }

        if (notNullAndNotEmpty(source.getLastName())) {
            target.setLastName(source.getLastName());
        }

        if (source.getSex() != null) {
            target.setSex(source.getSex());
        }

        if (notNullAndNotEmpty(source.getStatus())) {
            target.setStatus(source.getStatus());
        }

        if (notNullAndNotEmpty(source.getEmail())) {
            if (!source.getEmail().equals(target.getEmail())) {
                if (existsByEmail(source.getEmail())) {
                    throw new UserInfoServiceException("User info with such email already exists");
                }
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
