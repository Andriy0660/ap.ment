package com.example.ap_ment.service;

import com.example.ap_ment.dto.response.UserDTO;
import com.example.ap_ment.entity.FriendRequest;
import com.example.ap_ment.entity.User;
import com.example.ap_ment.exception.BadRequestException;
import com.example.ap_ment.exception.ConflictException;
import com.example.ap_ment.exception.NotFoundException;
import com.example.ap_ment.mapper.FriendRequestMapper;
import com.example.ap_ment.mapper.UserMapper;
import com.example.ap_ment.repository.FriendRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendRequestRepository friendRequestRepository;
    private final UserService userService;
    private final FriendRequestMapper friendRequestMapper;
    private final UserMapper userMapper;

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
    public void confirmFriendRequest(User receiver, User sender){
        receiver.getFriends().add(sender);
        sender.getFriends().add(receiver);
        userService.save(sender);
        userService.save(receiver);
    }
    public Set<UserDTO> getAllFriendRequestDTOsForUser(User user){
        return friendRequestMapper.setToDTOs(user.getFriendRequests());
    }
    public Set<UserDTO> getAllFriendDTOsForUser(User user){
        return userMapper.setToDTOs(user.getFriends());
    }


}