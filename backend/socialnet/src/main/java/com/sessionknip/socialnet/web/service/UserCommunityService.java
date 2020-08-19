package com.sessionknip.socialnet.web.service;

import com.sessionknip.socialnet.web.domain.User;
import com.sessionknip.socialnet.web.domain.UserCommunity;
import com.sessionknip.socialnet.web.dto.UserCommunityStatus;
import com.sessionknip.socialnet.web.service.exception.CommunityException;

import java.util.List;

public interface UserCommunityService {

    List<User> getSubscriptions(User user, Integer page, Integer howMuch);
    List<User> getSubscribers(User user, Integer page, Integer howMuch);
    List<User> getFriends(User user, Integer page, Integer howMuch);
    List<User> getFriends(User user);
    void addSubscription(User user, User subscriber) throws CommunityException;
    void removeSubscription(User user, User subscription) throws CommunityException;
    void addFriend(User user, User friend) throws CommunityException;
    void removeFriend(User user, User friend) throws CommunityException;
    UserCommunityStatus getCommunityStatus(User forWho, User who);
    void update(UserCommunity community);
    void delete(Long id);

}
