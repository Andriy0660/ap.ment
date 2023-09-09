package com.example.ap_ment.service;

import com.example.ap_ment.entity.User;
import com.example.ap_ment.entity.UserDetailsImpl;
import com.example.ap_ment.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class UserDetailServiceImplTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserDetailServiceImpl userDetailService;

    @Test
    void shouldReturnUserDetailsBasedOnGivenUser() {
        User user = User.builder()
                .firstName("Andrii")
                .lastName("Snovyda")
                .email("sandriy@gmail.com")
                .password("1234567")
                .build();
        String email = user.getEmail();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        assertThat(userDetailService.loadUserByUsername(email)).isInstanceOf(UserDetailsImpl.class);
        assertThat(userDetailService.loadUserByUsername(email).getPassword()).isEqualTo(user.getPassword());
        verify(userRepository, Mockito.times(2)).findByEmail(email);

    }
    @Test
    void shouldThrowWhenUserNotFound(){
        String email = "email@email.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        assertThatThrownBy(()->userDetailService.loadUserByUsername(email))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found");
    }
}