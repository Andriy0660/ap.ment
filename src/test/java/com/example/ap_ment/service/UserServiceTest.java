package com.example.ap_ment.service;

import com.example.ap_ment.dto.response.UserDTO;
import com.example.ap_ment.entity.User;
import com.example.ap_ment.exception.BadRequestException;
import com.example.ap_ment.exception.NotFoundException;
import com.example.ap_ment.mapper.UserMapper;
import com.example.ap_ment.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class UserServiceTest {
    @Autowired
    private UserRepository userRepository;
    private UserService userService;
    private UserMapper userMapper= new UserMapper();
    private static User user;

    @BeforeEach
    void setUp() {userService = new UserService(userRepository,userMapper);}
    @BeforeAll
    static void buildUser(){
        user = User.builder()
                .firstName("Andrii")
                .lastName("Snovyda")
                .email("sandriy@gmail.com")
                .password("1234567")
                .friendRequestCode("HALLO")
                .build();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }
    @Test
    void shouldReturnTrueWhenEmailExists(){
        userRepository.save(user);
        assertThat(userService.existsByEmail(user.getEmail())).isTrue();
    }
    @Test
    void shouldReturnFalseWhenEmailDoesntExists(){
        userRepository.save(user);
        assertThat(userService.existsByEmail("hello@gmail.com")).isFalse();
    }
    @Test
    void shouldReturnUserWhenEmailExists() {
        userRepository.save(user);
        assertThat(userService.findByEmail(user.getEmail()).getFirstName()).isEqualTo("Andrii");
    }
    @Test
    void shouldThrowWhenEmailDoesntExist(){
        assertThatThrownBy(() -> userService.findByEmail("inna@gmail.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("There is no user with this email");
    }

    @Test
    void shouldReturnUserByUserId() {
        userRepository.save(user);
        assertThat(userService.findById(2).getEmail())
                .isEqualTo("sandriy@gmail.com");
    }
    @Test
    void shouldThrowWhenUserWithIdDoesntExist(){
        assertThatThrownBy(() -> userService.findById(1))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User with this id is not found");
    }
    @Test
    void shouldSaveUser(){
        userService.save(user);
        assertThat(userRepository.existsByEmail(user.getEmail())).isTrue();
    }
    @Test
    void shouldFIndUserByReqCode(){
        userRepository.save(user);
        UserDTO userDTO = UserDTO.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
        assertThat(userService.findByFriendRequestCode("HALLO",2)).isEqualTo(userDTO);
    }
    @Test
    void shouldThrowWhenUserWithReqCodeDoesntExist(){
        userRepository.save(user);
        assertThatThrownBy(()->userService.findByFriendRequestCode("BADCODE",2))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("There is no users with this code");
    }

    @Test
    void shouldThrowWhenUserWantToSearchHimself() {
        Integer idEqualsToTestedUser = 1;
        userRepository.save(user);
        assertThatThrownBy(()->userService.findByFriendRequestCode("HALLO",idEqualsToTestedUser))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("You can not add yourself to your friends");
    }

}