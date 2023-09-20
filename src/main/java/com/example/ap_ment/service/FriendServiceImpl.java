package com.example.ap_ment.service;

import com.example.ap_ment.dto.response.UserDTO;
import com.example.ap_ment.entity.FriendRequest;
import com.example.ap_ment.entity.User;
import com.example.ap_ment.exception.BadRequestException;
import com.example.ap_ment.exception.ConflictException;
import com.example.ap_ment.exception.NotFoundException;
import com.example.ap_ment.mapper.MapperManager;
import com.example.ap_ment.repository.FriendRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {
    private final FriendRequestRepository friendRequestRepository;
    private final UserServiceImpl userService;
    private final MapperManager mapperManager;

    public void save(FriendRequest friendRequest){
        friendRequestRepository.save(friendRequest);
    }
    public void delete(FriendRequest friendRequest){
        friendRequestRepository.delete(friendRequest);
    }
    public FriendRequest findById(Integer id){
        return friendRequestRepository.findById(id).orElseThrow(()->
                new NotFoundException("There is no request with this id"));
    }
    public ResponseEntity<Void> makeFriendRequest(Integer receiverId, User user){
        if(user.getId().equals(receiverId))
            throw new BadRequestException("You can not add yourself to your friends");
        FriendRequest friendRequest = FriendRequest.builder()
                .sender(user)
                .receiverId(receiverId)
                .build();
        save(friendRequest);
        return ResponseEntity.noContent().build();
    }
    public ResponseEntity<Void> performFriendRequest(String status, Integer friendRequestId, User receiver){
        FriendRequest friendRequest = findById(friendRequestId);

        checkReceiverId(friendRequest.getReceiverId(),receiver.getId());

        if(status.equals("confirm")){
            User sender = userService.findById(friendRequest.getSender().getId());
            confirmFriendRequest(receiver,sender);
        }
        delete(friendRequest);
        return ResponseEntity.noContent().build();
    }
    public void checkReceiverId(Integer receiverIdFromRequest, Integer actualReceiverId){
        if(!receiverIdFromRequest.equals(actualReceiverId))
            throw new ConflictException("Error. You are not the receiver of this friend request");
    }
    public void confirmFriendRequest(User sender, User receiver){
        receiver.getFriends().add(sender);
        sender.getFriends().add(receiver);
        userService.save(sender);
        userService.save(receiver);    }
    public Set<UserDTO> getAllFriendRequestsForUser(User user){
        return mapperManager.map(user.getFriendRequests());
    }
    public Set<UserDTO> getAllFriendsForUser(User user){
        return mapperManager.map(user.getFriends());
    }

}
