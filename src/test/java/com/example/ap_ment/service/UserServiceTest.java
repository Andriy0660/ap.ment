package com.example.ap_ment.service;

import com.example.ap_ment.entity.User;
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
    private static User user;

    @BeforeEach
    void setUp() {userService = new UserService(userRepository);}
    @BeforeAll
    static void buildUser(){
        user = User.builder()
                .firstName("Andrii")
                .lastName("Snovyda")
                .email("sandriy@gmail.com")
                .password("1234567")
                .build();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }
    @Test
    void shouldReturnTrueWhenEmailExists(){
        userRepository.save(user);
        assertThat(userService.existsUserByEmail(user.getEmail())).isTrue();
    }
    @Test
    void shouldReturnFalseWhenEmailDoesntExists(){
        userRepository.save(user);
        assertThat(userService.existsUserByEmail("hello@gmail.com")).isFalse();
    }
    @Test
    void shouldReturnUserWhenEmailExists() {
        userRepository.save(user);
        assertThat(userService.findUserByEmail(user.getEmail()).getFirstName()).isEqualTo("Andrii");
    }
    @Test
    void shouldThrowWhenEmailDoesntExist(){
        assertThatThrownBy(() -> userService.findUserByEmail("inna@gmail.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("There is no user with this email");
    }

    @Test
    void shouldReturnUserByUserId() {
        userRepository.save(user);
        assertThat(userService.findUserById(Long.valueOf(1)).getEmail())
                .isEqualTo("sandriy@gmail.com");
    }
    @Test
    void shouldThrowWhenUserWithIdDoesntExist(){
        assertThatThrownBy(() -> userService.findUserById(Long.valueOf(1)))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User with this id is not found");
    }
    @Test
    void shouldSaveUser(){
        userService.save(user);
        assertThat(userRepository.existsByEmail(user.getEmail())).isTrue();
    }
}