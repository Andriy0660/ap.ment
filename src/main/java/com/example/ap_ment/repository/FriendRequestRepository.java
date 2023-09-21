package com.example.ap_ment.repository;

import com.example.ap_ment.entity.FriendRequest;

import java.util.Optional;
public interface FriendRequestRepository{
    Optional<FriendRequest> findById(Integer id);
    FriendRequest save(FriendRequest friendRequest);
    int delete(FriendRequest friendRequest);
    int deleteAll();

    boolean existsBySenderIdAndReceiverId(Integer senderId, Integer receiverId);
}
