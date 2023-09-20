package com.example.ap_ment.mapper;

import com.example.ap_ment.entity.FriendRequest;
import com.example.ap_ment.entity.User;
import com.example.ap_ment.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MapperManagerTest {
    @Spy
    UserMapper userMapper = new UserMapper();
    @Spy
    FriendRequestMapper friendRequestMapper = new FriendRequestMapper(userMapper);
    MapperManager mapperManager;

    @BeforeEach
    void setUp() {
        List<IMapper> mappers = new ArrayList<>();
        mappers.add(userMapper);
        mappers.add(friendRequestMapper);
        mapperManager = new MapperManager(mappers);
        //when(userMapper.supports(new User())).thenReturn(true);
    }

    @Test
    void shouldInvokeMapUser() {
        User user = new User();
        mapperManager.map(user);
        verify(userMapper).map(user);
    }
    @Test
    void shouldInvokeMapSetOfUsers() {
        User user1 = new User();
        User user2 = new User();
        Set<User>set = new HashSet<>();
        set.add(user1);
        set.add(user2);
        mapperManager.map(set);
        verify(userMapper).map(set);
    }
    @Test
    void shouldInvokeMapFriendRequest() {
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(new User());
        mapperManager.map(friendRequest);
        verify(friendRequestMapper).map(friendRequest);
        verify(userMapper,never()).map(Mockito.any());
    }
    @Test
    void shouldInvokeMapSetOfFriendRequests() {
        FriendRequest friendRequest1 = new FriendRequest();
        friendRequest1.setSender(new User());

        FriendRequest friendRequest2 = new FriendRequest();
        friendRequest2.setSender(new User());

        Set<FriendRequest>set = new HashSet<>();
        set.add(friendRequest1);
        set.add(friendRequest2);
        mapperManager.map(set);
        verify(friendRequestMapper).map(set);
    }

    @Test
    void shouldThrowWhenUnsupportedType() {
        assertThatThrownBy(()->mapperManager.map(new Object()))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Unsupported type");
    }
}
