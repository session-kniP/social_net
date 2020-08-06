package com.sessionknip.socialnet.web.service;

import com.sessionknip.socialnet.web.domain.UserInfo;
import com.sessionknip.socialnet.web.service.exception.UserInfoException;

import java.util.List;

public interface UserInfoService {

    List<UserInfo> findAll();
    List<UserInfo> findByFirstName(String firstName);
    List<UserInfo> findByLastName(String lastName);
    UserInfo findByEmail(String email) throws UserInfoException;
    boolean existsByEmail(String email);
    void edit(UserInfo target, UserInfo source) throws UserInfoException;
    UserInfo editNotSave(UserInfo target, UserInfo source) throws UserInfoException;
    void delete(Long userInfoId);

}
