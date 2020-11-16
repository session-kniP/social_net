package com.sessionknip.socialnet.web.service.impl;

import com.sessionknip.socialnet.web.domain.User;
import com.sessionknip.socialnet.web.domain.UserCommunity;
import com.sessionknip.socialnet.web.dto.community.UserCommunityStatus;
import com.sessionknip.socialnet.web.repository.UserCommunityRepo;
import com.sessionknip.socialnet.web.service.UserCommunityService;
import com.sessionknip.socialnet.web.service.exception.CommunityServiceException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
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

    public UserCommunityServiceImpl(UserCommunityRepo communityRepo, @Qualifier("userServiceImpl") UserServiceImpl userService) {
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
        List<User> community =
                communityRepo.findSubscriptionsByUserId(user.getUserCommunity().getId(), PageRequest.of(page, howMuch))
                        .stream().map(UserCommunity::getUser).collect(Collectors.toList());
        return community;
    }

    @Override
    public List<User> getSubscribers(User user, Integer page, Integer howMuch) {
        List<User> users =
                communityRepo.findSubscribersByUserId(user.getUserCommunity().getId(), PageRequest.of(page, howMuch))
                        .stream().map(UserCommunity::getUser).collect(Collectors.toList());
        return users;
    }

    @Override
    public List<User> getFriends(User user, Integer page, Integer howMuch) {
        //get half from user friends and half from friend of
        int half = howMuch / 2;
        List<User> firstPart =
                communityRepo.findIncomingFriendsByUserId(user.getUserCommunity().getId(), PageRequest.of(page, half))
                        .stream().map(UserCommunity::getUser).collect(Collectors.toList());
        List<User> secondPart =
                communityRepo.findOutgoingFriendsByUserId(user.getUserCommunity().getId(), PageRequest.of(page, howMuch - half))
                        .stream().map(UserCommunity::getUser).collect(Collectors.toList());

        return Stream.concat(firstPart.stream(), secondPart.stream()).collect(Collectors.toList());
    }

    @Override
    public List<User> getFriends(User user) {
        List<User> firstPart = communityRepo.findIncomingFriendsByUserId(user.getUserCommunity().getId())
                .stream().map(UserCommunity::getUser).collect(Collectors.toList());
        List<User> secondPart = communityRepo.findOutgoingFriendsByUserId(user.getUserCommunity().getId())
                .stream().map(UserCommunity::getUser).collect(Collectors.toList());

        return Stream.concat(firstPart.stream(), secondPart.stream()).collect(Collectors.toList());
    }

    //SUBSCRIPTION subscribes to USER
    @Override
    @Transactional
    public void addSubscription(User user, User subscription) throws CommunityServiceException {
        if (subscription.getUserCommunity().getId().equals(user.getUserCommunity().getId())) {
            throw new CommunityServiceException("You can't subscribe to yourself");
        }

        if (communityRepo.findSubscribersByUserId(subscription.getUserCommunity().getId()).contains(user.getUserCommunity())) {
            throw new CommunityServiceException("You are already subscribed to this user");
        }

        if (communityRepo.findSubscribersByUserId(user.getUserCommunity().getId()).contains(subscription.getUserCommunity())) {
            addFriend(user, subscription);
            return;
        }

        if (getFriends(user).contains(subscription)) {
            throw new CommunityServiceException("This user is already in your friends list");
        }

        //make it by IDs of user COMMUNITY, but NOT the user!
        // User ID an user community ID could be different
        communityRepo.addSubscription(subscription.getUserCommunity().getId(), user.getUserCommunity().getId());
    }

    //SUBSCRIPTION unsubscribes USER
    @Override
    @Transactional
    public void removeSubscription(User user, User subscription) throws CommunityServiceException {
        if (!communityRepo.findSubscriptionsByUserId(user.getUserCommunity().getId()).contains(subscription.getUserCommunity())) {
            throw new CommunityServiceException("You don't subscribed to this user");
        }

        communityRepo.removeSubscriptionById(subscription.getUserCommunity().getId(), user.getUserCommunity().getId());
    }


    @Override
    @Transactional
    public void addFriend(User user, User friend) throws CommunityServiceException {
        if (!communityRepo.findSubscribersByUserId(user.getUserCommunity().getId()).contains(friend.getUserCommunity())) {
            throw new CommunityServiceException("This user is not subscribed to you");
        }

        if (getFriends(user).contains(friend)) {
            throw new CommunityServiceException("This user is already in your friends list");
        }

        //remove SUBSCRIBER(friend) by SUBSCRIPTION(user)
        communityRepo.removeSubscriberById(friend.getUserCommunity().getId(), user.getUserCommunity().getId());
        communityRepo.addFriend(user.getUserCommunity().getId(), friend.getUserCommunity().getId());
    }

    @Override
    @Transactional
    public void removeFriend(User user, User friend) throws CommunityServiceException {
        if (!getFriends(user).contains(friend)) {
            throw new CommunityServiceException("This user is not in your friends list");
        }

        communityRepo.removeIncomingFriendById(friend.getUserCommunity().getId(), user.getUserCommunity().getId());
        communityRepo.removeOutgoingFriendById(friend.getUserCommunity().getId(), user.getUserCommunity().getId());

        addSubscription(friend, user);

        userService.update(user);
    }

    @Override
    @Transactional
    public UserCommunityStatus getCommunityStatus(User forWho, User who) {
        //todo All of conditions should be implemented through user community repo using 'in' statement

        if (communityRepo.findSubscriptionsWithObjectByUserId(who.getUserCommunity(), forWho.getUserCommunity().getId(), PageRequest.of(0, 1)).size() != 0) {
            return UserCommunityStatus.SUBSCRIPTION;
        }

        if (communityRepo.findSubscribersWithObjectByUserId(who.getUserCommunity(), forWho.getUserCommunity().getId(), PageRequest.of(0, 1)).size() != 0) {
            return UserCommunityStatus.SUBSCRIBER;
        }

        if (getFriends(forWho).contains(who)) {
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
