package com.sessionknip.socialnet.web.repository;

import com.sessionknip.socialnet.web.domain.User;
import com.sessionknip.socialnet.web.domain.UserCommunity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserCommunityRepo extends JpaRepository<UserCommunity, Long> {

    @Query(value = "from UserCommunity uc where uc.user=:user")
    UserCommunity findByUser(@Param("user") User user);

    @Query(value = "from UserCommunity uc join uc.user u where u.id=:userId")
    UserCommunity findByUserId(@Param("userId") Long userId);

    @Query(value = "select uc.subscriptions from UserCommunity uc where uc.id=:userId")
    Page<UserCommunity> findSubscriptionsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "select uc.subscriptions from UserCommunity uc where uc.id=:userId")
    List<UserCommunity> findSubscriptionsByUserId(@Param("userId") Long userId);

    @Query(value = "select uc.subscribers from UserCommunity uc where uc.id=:userId")
    Page<UserCommunity> findSubscribersByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "select uc.subscribers from UserCommunity uc where uc.id=:userId")
    List<UserCommunity> findSubscribersByUserId(@Param("userId") Long userId);

    @Query(value = "select uc.incomingFriends from UserCommunity uc where uc.id=:userId")
    Page<UserCommunity> findIncomingFriendsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "select uc.incomingFriends from UserCommunity uc where uc.id=:userId")
    List<UserCommunity> findIncomingFriendsByUserId(@Param("userId") Long userId);

    @Query(value = "select uc.outgoingFriends from UserCommunity uc where uc.id=:userId")
    Page<UserCommunity> findOutgoingFriendsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "select uc.outgoingFriends from UserCommunity uc where uc.id=:userId")
    List<UserCommunity> findOutgoingFriendsByUserId(@Param("userId") Long userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "insert into user_subs(subscriber_id, subscription_id) values (:subscriber_id, :subscription_id)", nativeQuery = true)
    void addSubscriber(@Param("subscriber_id") Long subscriberId, @Param("subscription_id") Long subscriptionId);

    @Modifying(clearAutomatically = true)
    @Query(value = "insert into user_subs(subscription_id, subscriber_id) values (:subscription_id, :subscriber_id)", nativeQuery = true)
    void addSubscription(@Param("subscription_id") Long subscriptionId, @Param("subscriber_id") Long subscriberId);

    @Modifying(clearAutomatically = true)
    @Query(value = "insert into user_friends(incoming_friend_id, outgoing_friend_id) values (:incoming_friend_id, :outgoing_friend_id)", nativeQuery = true)
    void addFriend(@Param("incoming_friend_id") Long incomingFriendId, @Param("outgoing_friend_id") Long outgoingFriendId);

    @Modifying(clearAutomatically = true)
    @Query(value = "delete from user_subs where subscriber_id=:subscriber_id and subscription_id=:subscription_id", nativeQuery = true)
    void removeSubscriberById(@Param("subscriber_id") Long subscriberId, @Param("subscription_id") Long subscriptionId);

    @Modifying(clearAutomatically = true)
    @Query(value = "delete from user_subs where subscription_id=:subscription_id and subscriber_id=:subscriber_id", nativeQuery = true)
    void removeSubscriptionById(@Param("subscription_id") Long subscriptionId, @Param("subscriber_id") Long subscriberId);

    @Modifying(clearAutomatically = true)
    @Query(value = "delete from user_friends where incoming_friend_id=:incoming_friend_id and outgoing_friend_id=:outgoing_friend_id", nativeQuery = true)
    void removeIncomingFriendById(@Param("incoming_friend_id") Long incomingFriendId, @Param("outgoing_friend_id") Long outgoingFriendId);

    @Modifying(clearAutomatically = true)
    @Query(value = "delete from user_friends where outgoing_friend_id=:outgoing_friend_id and incoming_friend_id=:incoming_friend_id", nativeQuery = true)
    void removeOutgoingFriendById(@Param("outgoing_friend_id") Long outgoingFriendId, @Param("incoming_friend_id") Long incomingFriendId);

    @Query(value = "select uc.subscriptions from UserCommunity uc where :subscription member of uc.subscriptions and uc.id=:id")
    List<User> findSubscriptionsWithObjectByUserId(@Param("subscription") UserCommunity userCommunity, @Param("id") Long id, @PageableDefault(size = 1) Pageable pageable);

    @Query(value = "select uc.subscriptions from UserCommunity uc where :subscriber member of uc.subscribers and uc.id=:id")
    List<User> findSubscribersWithObjectByUserId(@Param("subscriber") UserCommunity userCommunity, @Param("id") Long id, @PageableDefault(size = 1) Pageable pageable);
}
