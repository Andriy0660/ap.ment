package com.example.ap_ment.service;

import com.example.ap_ment.dto.response.UserDTO;
import com.example.ap_ment.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.Set;

public interface FriendService {
    ResponseEntity<Void> makeFriendRequest(User sender, Integer receiverId);
    ResponseEntity<Void> performFriendRequest(String status, Integer friendRequestId, User receiver);
    Set<UserDTO> getAllFriendRequestsForUser(User user);
    Set<UserDTO> getAllFriendsForUser(User user);
}
