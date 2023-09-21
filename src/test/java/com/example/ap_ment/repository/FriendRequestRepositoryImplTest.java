package com.example.ap_ment.repository;

import com.example.ap_ment.entity.FriendRequest;
import com.example.ap_ment.entity.User;
import com.example.ap_ment.exception.BadRequestException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
class FriendRequestRepositoryImplTest {
    @Autowired
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        friendRequestRepository.deleteAll();
        userRepository.deleteAll();
    }
    @Test
    void shouldDeleteFriendRequest() {
        User sender = User.builder().email("A").build();
        User receiver = User.builder().email("B").build();
        User resSender = userRepository.save(sender);
        User resReceiver = userRepository.save(receiver);

        FriendRequest friendRequest = FriendRequest.builder()
                .sender(resSender)
                .receiverId(resReceiver.getId())
                .build();
        FriendRequest resFriendRequest = friendRequestRepository.save(friendRequest);

        assertThat(resFriendRequest.getSender()).isEqualTo(resSender);
        friendRequestRepository.delete(resFriendRequest);
        assertThat(friendRequestRepository.findById(resFriendRequest.getId())).isEqualTo(Optional.empty());

    }

    @Test
    void shouldSaveFriendRequestAndFindUser() {
        User sender = User.builder().email("A").build();
        User receiver = User.builder().email("B").build();
        User resSender = userRepository.save(sender);
        User resReceiver = userRepository.save(receiver);

        FriendRequest friendRequest = FriendRequest.builder()
                .sender(resSender)
                .receiverId(resReceiver.getId())
                .build();
        FriendRequest resFriendRequest = friendRequestRepository.save(friendRequest);

        assertThat(friendRequestRepository.findById(resFriendRequest.getId()).get()
                .getReceiverId()).isEqualTo(resReceiver.getId());
    }

    @Test
    void shouldReturnEmptyOptional() {
        assertThat(friendRequestRepository.findById(1)).isEqualTo(Optional.empty());
    }

    @Test
    void shouldReturnTrueWhenExistsBySenderIdAndReceiverId() {
        User sender = User.builder().email("A").build();
        User receiver = User.builder().email("B").build();
        User resSender = userRepository.save(sender);
        User resReceiver = userRepository.save(receiver);

        FriendRequest friendRequest = FriendRequest.builder()
                .sender(resSender)
                .receiverId(resReceiver.getId())
                .build();
        FriendRequest resFriendRequest = friendRequestRepository.save(friendRequest);
        assertThat(friendRequestRepository.existsBySenderIdAndReceiverId
                (resFriendRequest.getSender().getId(),resFriendRequest.getReceiverId())).isTrue();
    }

    @Test
    void shouldReturnFalseWhenExistsBySenderIdAndReceiverId() {
        assertThat(friendRequestRepository.existsBySenderIdAndReceiverId
                (1,2)).isFalse();
    }
}