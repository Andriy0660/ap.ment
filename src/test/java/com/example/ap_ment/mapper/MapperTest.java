package com.example.ap_ment.mapper;

import com.example.ap_ment.dto.response.UserDTO;
import com.example.ap_ment.entity.FriendRequest;
import com.example.ap_ment.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MapperTest {
    @Mock
    private UserMapper userMapper;
    @Mock
    private FriendRequestMapper friendRequestMapper;
    @InjectMocks
    private Mapper mapper;
    static private User user1;
    static private User user2;
    static private FriendRequest friendRequest1;
    static private FriendRequest friendRequest2;
    @BeforeAll
    static void setUp() {
        user1 = User.builder()
                .id(1)
                .firstName("Andrii")
                .lastName("Snovyda")
                .email("sandriy@gmail.com")
                .password("1234567")
                .friendRequestCode("HALLO")
                .friends(new HashSet<>())
                .friendRequests(new HashSet<>())
                .build();
        user2 = User.builder()
                .id(2)
                .firstName("Zakhar")
                .lastName("Rapipca")
                .email("zakhar@gmail.com")
                .password("1234567")
                .friendRequestCode("HELLO")
                .friendRequests(new HashSet<>())
                .friends(new HashSet<>())
                .build();
        friendRequest1 = FriendRequest.builder()
                .sender(user1)
                .receiverId(2)
                .build();
        friendRequest1 = FriendRequest.builder()
                .sender(user2)
                .receiverId(1)
                .build();
    }

    @Test
    void shouldInvokeUserToDTO() {
        mapper.map(user1, UserDTO.class);
        verify(userMapper).userToDTO(user1);
    }
    @Test
    void shouldInvokeSetToDTOs() {
        Set<User> users = new HashSet<>();
        users.add(user1);
        users.add(user2);
        mapper.map(users, Set.class);
        verify(userMapper).setToDTOs(users);
    }
    @Test
    void shouldInvokeFriendRequestToUserDTO() {
        mapper.map(friendRequest1, UserDTO.class);
        verify(friendRequestMapper).friendRequestToUserDTO(friendRequest1);
    }
    @Test
    void shouldInvokeSetOfFriendRequestsToDTOs() {
        Set<FriendRequest> requests = new HashSet<>();
        requests.add(friendRequest1);
        requests.add(friendRequest1);
        mapper.map(requests, Set.class);
        verify(friendRequestMapper).setToDTOs(requests);
    }
    //TODO: write tests for checking exceptions
}