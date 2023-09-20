package com.example.ap_ment.mapper;

import com.example.ap_ment.dto.response.UserDTO;
import com.example.ap_ment.entity.FriendRequest;
import com.example.ap_ment.entity.User;
import org.junit.jupiter.api.Test;


import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class FriendRequestMapperTest {
    UserMapper userMapper = new UserMapper();
    FriendRequestMapper friendRequestMapper=new FriendRequestMapper(userMapper);

    @Test
    void shouldTransformFriendReqToUserDTO(){
        User user = User.builder()
                .firstName("Andrii")
                .lastName("Snovyda")
                .email("sandriy@gmail.com")
                .password("1234567")
                .friendRequestCode("HALLO")
                .build();
        UserDTO userDTO = UserDTO.builder()
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .email(user.getEmail())
                .build();
        FriendRequest friendRequest = FriendRequest.builder()
                .sender(user)
                .receiverId(1)
                .build();
        assertThat((UserDTO)friendRequestMapper.map(friendRequest)).isEqualTo(userDTO);
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
        assertThat((Set<UserDTO>)friendRequestMapper.map(requests)).isEqualTo(expected);
    }

    @Test
    void shouldReturnTrueWhenSupportsFriendRequest() {
        assertThat(friendRequestMapper.supports(new FriendRequest())).isTrue();
    }
    @Test
    void shouldReturnTrueWhenSupportsSetOfFriendRequests() {
        Set<FriendRequest> set = new HashSet<>();
        set.add(new FriendRequest());
        set.add(new FriendRequest());
        assertThat(friendRequestMapper.supports(set)).isTrue();
    }
    @Test
    void shouldReturnFalseWhenSupportsOtherType() {
        assertThat(friendRequestMapper.supports(new User())).isFalse();
    }
    @Test
    void shouldReturnFalseWhenSupportsSetOfOtherTypes() {
        Set<User> set = new HashSet<>();
        set.add(new User());
        set.add(new User());
        assertThat(friendRequestMapper.supports(set)).isFalse();
    }
    @Test
    void shouldReturnUserDTOWhenGivenFriendRequest() {
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(new User());
        assertDoesNotThrow(()->friendRequestMapper.map(friendRequest));
    }
    @Test
    void shouldReturnSetOfUsersDTOWhenGivenSetOfUsers() {
        Set<FriendRequest> requests = new HashSet<>();
        FriendRequest friendRequest1 = new FriendRequest();
        friendRequest1.setSender(new User());
        FriendRequest friendRequest2 = new FriendRequest();
        friendRequest2.setSender(new User());
        requests.add(friendRequest1);
        requests.add(friendRequest2);

        assertDoesNotThrow(()->friendRequestMapper.map(requests));
        assertThat((Set<?>)friendRequestMapper.map(requests))
                .isInstanceOf(Set.class);

        assertThat((((Set<?>) friendRequestMapper.map(requests)).iterator().next()))
                .isInstanceOf(UserDTO.class);
    }
}