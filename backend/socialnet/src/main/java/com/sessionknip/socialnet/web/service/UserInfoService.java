package com.sessionknip.socialnet.web.service;

import com.sessionknip.socialnet.web.domain.UserInfo;
import com.sessionknip.socialnet.web.service.exception.UserInfoServiceException;

import java.util.List;

public interface UserInfoService {

    List<UserInfo> findAll();
    List<UserInfo> findByFirstName(String firstName);
    List<UserInfo> findByLastName(String lastName);
    UserInfo findByEmail(String email) throws UserInfoServiceException;
    List<UserInfo> findByFilters(Integer page, Integer howMuch, String[] filters);
    List<UserInfo> findSeveral(Integer page, Integer howMuch);
    boolean existsByEmail(String email);
    void edit(UserInfo target, UserInfo source) throws UserInfoServiceException;
    UserInfo editNotSave(UserInfo target, UserInfo source) throws UserInfoServiceException;
    void delete(Long userInfoId);

}
