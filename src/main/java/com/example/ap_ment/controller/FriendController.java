package com.example.ap_ment.controller;

import com.example.ap_ment.dto.response.UserDTO;
import com.example.ap_ment.entity.User;
import com.example.ap_ment.entity.UserDetailsImpl;
import com.example.ap_ment.exception.NotFoundException;
import com.example.ap_ment.service.FriendService;
import com.example.ap_ment.service.UserService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

@RestController
@RequestMapping("apment/v1/friends")
@RequiredArgsConstructor
@Validated
public class FriendController {
    private final FriendService friendService;
    private final UserService userService;

    @GetMapping("/search")
    public ResponseEntity<?>searchByCode(@RequestParam("code")String code){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        try {
            return ResponseEntity.ok(userService.findByFriendRequestCode(code, user.getId()));
        } catch(NotFoundException e){
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping
    public ResponseEntity<Void>makeRequest(@RequestParam("id")Integer friendId){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        return friendService.makeFriendRequest(friendId,user);
    }

    @PutMapping
    public ResponseEntity<Void> performFriendRequest(@RequestParam("status") @Pattern(regexp = "confirm|reject",
        message = "'status' must be 'confirm' or 'reject'")String status,
                                            @RequestParam("id")Integer friendRequestId){

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        User receiver = userDetails.getUser();
        return friendService.performFriendRequest(status,friendRequestId,receiver);
    }

    @GetMapping("/requests")
    public ResponseEntity<Set<UserDTO>> getAllFriendRequestsForUser(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        return ResponseEntity.ok(friendService.getAllFriendRequestDTOsForUser(user));
    }

    @GetMapping
    public ResponseEntity<Set<UserDTO>> getAllFriendsForUser(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        return ResponseEntity.ok(friendService.getAllFriendDTOsForUser(user));
    }

}
