package com.example.ap_ment.mapper;

import com.example.ap_ment.dto.response.UserDTO;
import com.example.ap_ment.entity.FriendRequest;
import com.example.ap_ment.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
@ExtendWith(MockitoExtension.class)
class FriendRequestMapperTest {
    @Spy
    UserMapper userMapper = new UserMapper();
    @InjectMocks
    FriendRequestMapper friendRequestMapper;

    @Test
    void shouldTransformFriendReqToUserDTO(){
        User user = User.builder()
                .firstName("Andrii")
                .lastName("Snovyda")
                .email("sandriy@gmail.com")
                .password("1234567")
                .friendRequestCode("HALLO")
                .build();
        FriendRequest friendRequest = FriendRequest.builder()
                .sender(user)
                .receiverId(1)
                .build();
        friendRequestMapper.friendRequestToUserDTO(friendRequest);
        verify(userMapper).userToDTO(user);
    }
    @Test
    void shouldTransformSetOfFriendReqsToUserDTOs(){
        User user1 = User.builder()
                .firstName("Andrii")
                .lastName("Snovyda")
                .email("sandriy@gmail.com")
                .password("1234567")
                .friendRequestCode("HALLO")
                .build();
        UserDTO dto1 = UserDTO.builder()
                .email(user1.getEmail())
                .firstName(user1.getFirstName())
                .lastName(user1.getLastName())
                .build();
        FriendRequest friendRequest1 = FriendRequest.builder()
                .sender(user1)
                .receiverId(2)
                .build();

        User user2 = User.builder()
                .firstName("Zakhar")
                .lastName("Rapipca")
                .email("zakhar@gmail.com")
                .password("1234567")
                .friendRequestCode("HELLO")
                .build();
        UserDTO dto2 = UserDTO.builder()
                .email(user2.getEmail())
                .firstName(user2.getFirstName())
                .lastName(user2.getLastName())
                .build();
        FriendRequest friendRequest2 = FriendRequest.builder()
                .sender(user2)
                .receiverId(1)
                .build();

        Set<FriendRequest> requests = new HashSet<>();
        requests.add(friendRequest1);
        requests.add(friendRequest2);

        Set<UserDTO> expected = new HashSet<>();
        expected.add(dto1);
        expected.add(dto2);
        assertThat(friendRequestMapper.setToDTOs(requests)).isEqualTo(expected);
    }
}