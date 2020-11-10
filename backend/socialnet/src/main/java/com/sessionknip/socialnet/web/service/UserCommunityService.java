package com.sessionknip.socialnet.web.service;

import com.sessionknip.socialnet.web.domain.User;
import com.sessionknip.socialnet.web.domain.UserCommunity;
import com.sessionknip.socialnet.web.dto.community.UserCommunityStatus;
import com.sessionknip.socialnet.web.service.exception.CommunityServiceException;

import java.util.List;

public interface UserCommunityService {

    UserCommunity getByUser(User user);
    UserCommunity getByUserId(Long userId);
    List<User> getSubscriptions(User user, Integer page, Integer howMuch);
    List<User> getSubscribers(User user, Integer page, Integer howMuch);
    List<User> getFriends(User user, Integer page, Integer howMuch);
    List<User> getFriends(User user);
    void addSubscription(User user, User subscriber) throws CommunityServiceException;
    void removeSubscription(User user, User subscription) throws CommunityServiceException;
    void addFriend(User user, User friend) throws CommunityServiceException;
    void removeFriend(User user, User friend) throws CommunityServiceException;
    UserCommunityStatus getCommunityStatus(User forWho, User who);
    void update(UserCommunity community);
    void delete(Long id);

}
