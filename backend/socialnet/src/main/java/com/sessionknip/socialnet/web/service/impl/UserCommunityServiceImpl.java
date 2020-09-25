package com.sessionknip.socialnet.web.service.impl;

import com.sessionknip.socialnet.web.domain.User;
import com.sessionknip.socialnet.web.domain.UserCommunity;
import com.sessionknip.socialnet.web.dto.UserCommunityStatus;
import com.sessionknip.socialnet.web.repository.UserCommunityRepo;
import com.sessionknip.socialnet.web.service.UserCommunityService;
import com.sessionknip.socialnet.web.service.exception.CommunityServiceException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("userCommunityServiceImpl")
public class UserCommunityServiceImpl implements UserCommunityService {

    private final UserCommunityRepo communityRepo;
    private final UserServiceImpl userService;

    public UserCommunityServiceImpl(UserCommunityRepo communityRepo, UserServiceImpl userService) {
        this.communityRepo = communityRepo;
        this.userService = userService;
    }

    @Override
    public UserCommunity getByUser(User user) {
        UserCommunity userCommunity = communityRepo.findByUser(user);
        if (userCommunity == null) {
            userCommunity = new UserCommunity(user);
            communityRepo.save(userCommunity);
        }
        return userCommunity;
    }

    @Override
    public UserCommunity getByUserId(Long userId) {
        UserCommunity userCommunity = communityRepo.findByUserId(userId);
        if (userCommunity == null) {
            userCommunity = new UserCommunity();
            communityRepo.save(userCommunity);
        }
        return userCommunity;
    }

    @Override
    public List<User> getSubscriptions(User user, Integer page, Integer howMuch) {
        return communityRepo.findSubscriptionsByUserId(user.getId(), PageRequest.of(page, howMuch)).getContent();
    }

    @Override
    public List<User> getSubscribers(User user, Integer page, Integer howMuch) {
        return communityRepo.findSubscribersByUserId(user.getId(), PageRequest.of(page, howMuch)).getContent();
    }

    @Override
    public List<User> getFriends(User user, Integer page, Integer howMuch) {
        //get half from user friends and half from friend of
        int half = howMuch / 2;
        List<User> firstPart = communityRepo.findIncomingFriendsByUserId(user.getId(), PageRequest.of(page, half)).getContent();
        List<User> secondPart = communityRepo.findOutgoingFriendsByUserId(user.getId(), PageRequest.of(page, howMuch - half)).getContent();

        return Stream.concat(firstPart.stream(), secondPart.stream()).collect(Collectors.toList());
    }

    @Override
    public List<User> getFriends(User user) {
        List<User> firstPart = communityRepo.findIncomingFriendsByUserId(user.getId());
        List<User> secondPart = communityRepo.findOutgoingFriendsByUserId(user.getId());

        return Stream.concat(firstPart.stream(), secondPart.stream()).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addSubscription(User user, User subscription) throws CommunityServiceException {
        if (subscription.getId().equals(user.getId())) {
            throw new CommunityServiceException("You can't subscribe to yourself");
        }

        if (communityRepo.findSubscribersByUserId(user.getId()).contains(subscription)) {
            throw new CommunityServiceException("You are already subscribed to this user");
        }

        if (getFriends(user).contains(subscription)) {
            throw new CommunityServiceException("This user is already in your friends list");
        }

        communityRepo.addSubscription(subscription.getId(), user.getId());
    }

    @Override
    @Transactional
    public void removeSubscription(User user, User subscription) throws CommunityServiceException {
        if (!communityRepo.findSubscribersByUserId(user.getId()).contains(subscription)) {
            throw new CommunityServiceException("You don't subscribed to this user");
        }

        communityRepo.removeSubscriptionById(subscription.getId());
    }

    @Override
    @Transactional
    public void addFriend(User user, User friend) throws CommunityServiceException {
        if (!communityRepo.findSubscribersByUserId(user.getId()).contains(friend)) {
            throw new CommunityServiceException("This user is not subscribed to you");
        }

        if (getFriends(user).contains(friend)) {
            throw new CommunityServiceException("This user is already in your friends list");
        }

        communityRepo.removeSubscriberById(friend.getId());
        communityRepo.addFriend(user.getId(), friend.getId());
    }

    @Override
    @Transactional
    public void removeFriend(User user, User friend) throws CommunityServiceException {
        if (!getFriends(user).contains(friend)) {
            throw new CommunityServiceException("This user is not in your friends list");
        }

        communityRepo.removeIncomingFriendById(friend.getId());
        communityRepo.removeOutgoingFriendById(friend.getId());

        user.getUserCommunity().getSubscribers().add(friend);

        userService.update(user);
    }

    @Override
    @Transactional
    public UserCommunityStatus getCommunityStatus(User forWho, User who) {
        //todo All if conditions should be implemented through user community repo using 'in' statement

        if (communityRepo.findSubscriptionsWithObjectByUserId(who, forWho.getUserCommunity().getId(), PageRequest.of(0, 1)).size() != 0) {
            return UserCommunityStatus.SUBSCRIPTION;
        }

        if (communityRepo.findSubscribersWithObjectByUserId(who, forWho.getUserCommunity().getId(), PageRequest.of(0, 1)).size() != 0) {
            return UserCommunityStatus.SUBSCRIBER;
        }

        if (forWho.getUserCommunity().getUserFriends().contains(who)) {
            return UserCommunityStatus.FRIEND;
        }

        return UserCommunityStatus.NONE;
    }

    @Override
    public void update(UserCommunity community) {
        communityRepo.save(community);
    }

    @Override
    public void delete(Long id) {
        communityRepo.deleteById(id);
    }
}
