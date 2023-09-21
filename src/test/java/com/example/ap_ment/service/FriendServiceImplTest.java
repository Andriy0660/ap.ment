package com.example.ap_ment.service;

import com.example.ap_ment.dto.response.UserDTO;
import com.example.ap_ment.entity.FriendRequest;
import com.example.ap_ment.entity.User;
import com.example.ap_ment.exception.BadRequestException;
import com.example.ap_ment.exception.ConflictException;
import com.example.ap_ment.exception.NotFoundException;
import com.example.ap_ment.mapper.MapperManager;
import com.example.ap_ment.repository.FriendRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FriendServiceImplTest {
    @Mock
    private FriendRequestRepository friendRequestRepository;
    @Spy
    @InjectMocks
    private FriendServiceImpl friendService;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private MapperManager mapperManager;

    private FriendRequest friendRequest;
    private User receiver;
    private User sender;
    @BeforeEach
    void setUp() {
        receiver = User.builder()
                .id(1)
                .firstName("Andrii")
                .lastName("Snovyda")
                .email("sandriy@gmail.com")
                .password("1234567")
                .friendRequestCode("HALLO")
                .friends(new HashSet<>())
                .friendRequests(new HashSet<>())
                .build();
        sender = User.builder()
                .id(2)
                .firstName("Zakhar")
                .lastName("Rapipca")
                .email("zakhar@gmail.com")
                .password("1234567")
                .friendRequestCode("HELLO")
                .friendRequests(new HashSet<>())
                .friends(new HashSet<>())
                .build();

        friendRequest = FriendRequest.builder()
                .sender(sender)
                .receiverId(1)
                .build();
    }
    @Test
    void save() {
        friendService.save(friendRequest);
        verify(friendRequestRepository).save(friendRequest);
    }

    @Test
    void delete() {
        friendService.delete(friendRequest);
        verify(friendRequestRepository).delete(friendRequest);
    }
    @Test
    void shouldFindFriendRequestById(){
        when(friendRequestRepository.findById(1)).thenReturn(Optional.of(friendRequest));
        assertThat(friendService.findById(1).getSender().getFirstName())
                .isEqualTo("Zakhar");
    }
    @Test
    void shouldThrowWhenFriendRequestDoesntExist() {
        when(friendRequestRepository.findById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(()-> friendService.findById(1))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("There is no request with this id");
    }
    @Test
    @WithMockUser(username = "sandriy@gmail.com", password = "1234567")
    void shouldPerformFriendRequest(){
        String status = "confirm";
        Integer friendRequestId = 1;
        Integer receiverId = 1;
        Integer senderId = 2;

        when(friendRequestRepository.findById(friendRequestId)).thenReturn(Optional.of(friendRequest));
        when(userService.findById(senderId)).thenReturn(sender);

        assertThat(friendService.performFriendRequest(status,friendRequestId,receiver))
                .isInstanceOf(ResponseEntity.class);
        verify(friendRequestRepository).findById(friendRequestId);
        verify(friendService).checkReceiverId(friendRequest.getReceiverId(),receiverId);
        verify(userService).findById(senderId);
        verify(friendService).confirmFriendRequest(receiver,sender);
        verify(friendService).delete(friendRequest);
    }
    @Test
    @WithMockUser
    void shouldNOTPerformFriendRequest(){
        String status = "reject";
        Integer friendRequestId = 1;
        Integer receiverId = 1;
        Integer senderId = 2;

        when(friendRequestRepository.findById(friendRequestId)).thenReturn(Optional.of(friendRequest));

        assertThat(friendService.performFriendRequest(status,friendRequestId,receiver))
                .isInstanceOf(ResponseEntity.class);
        verify(friendRequestRepository).findById(friendRequestId);
        verify(friendService).checkReceiverId(friendRequest.getReceiverId(),receiverId);

        //difference
        verify(userService, never()).findById(senderId);
        verify(friendService,never()).confirmFriendRequest(receiver,sender);

        verify(friendService).delete(friendRequest);
    }

    @Test
    void shouldThrowWhenReceiverIdsAreNotSame() {
        assertThatThrownBy(()-> friendService.checkReceiverId(1,2))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Error. You are not the receiver of this friend request");
    }
    @Test
    void shouldThrowWhenFriendRequestAlreadyExists() {
        User user = User.builder().id(1).build();
        when(friendRequestRepository.existsBySenderIdAndReceiverId(Mockito.any(),Mockito.any())).thenReturn(true);
        assertThatThrownBy(()-> friendService.makeFriendRequest(user,2))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("You`ve already sent a friend request to this user");
    }
    @Test
    void shouldNOTThrowWhenFriendRequestDOESNOTExists() {
        User user = User.builder().id(1).build();
        when(friendRequestRepository.existsBySenderIdAndReceiverId(Mockito.any(),Mockito.any())).thenReturn(false);
        assertDoesNotThrow(()-> friendService.makeFriendRequest(user,2));
    }

    @Test
    void shouldAddFriendAndSaveInDBForTwoUsers() {
        friendService.confirmFriendRequest(sender,receiver);
        assertThat(sender.getFriends().contains(receiver)).isTrue();
        assertThat(receiver.getFriends().contains(sender)).isTrue();
        verify(userService).save(sender);
        verify(userService).save(receiver);
    }

    @Test
    void shouldReturnAllFriendRequestsForUserTransformedIntoUserDTO() {
        receiver.getFriendRequests().add(friendRequest);

        Set<UserDTO> expected = new HashSet<>();
        expected.add(new UserDTO(sender.getEmail(), sender.getFirstName(), sender.getLastName()));

        when(mapperManager.map(Mockito.anySet())).thenReturn(expected);

        assertThat(friendService.getAllFriendRequestsForUser(receiver)).isEqualTo(expected);
    }
    @Test
    void shouldReturnAllFriendsForUserTransformedIntoUserDTO() {
        receiver.getFriends().add(sender);

        Set<UserDTO> expected = new HashSet<>();
        expected.add(new UserDTO(sender.getEmail(), sender.getFirstName(), sender.getLastName()));

        when(mapperManager.map(Mockito.anySet())).thenReturn(expected);

        assertThat(friendService.getAllFriendsForUser(receiver)).isEqualTo(expected);
    }

    @Test
    void shouldMakeFriendRequest() {
        Integer receiverId=1;
        assertThat(friendService.makeFriendRequest(sender,receiverId))
                .isEqualTo(ResponseEntity.noContent().build());
        verify(friendService).save(friendRequest);
    }

    @Test
    void shouldThrowWhenUserTryingToAddHimselfToFriends() {
        Integer idEqualsToTestedUser = 1;
        assertThatThrownBy(()-> friendService.makeFriendRequest(receiver,idEqualsToTestedUser))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("You can not add yourself to your friends");
        verify(friendService,never()).save(friendRequest);
    }
}