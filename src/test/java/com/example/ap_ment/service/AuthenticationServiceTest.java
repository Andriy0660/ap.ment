package com.example.ap_ment.service;

import com.example.ap_ment.dto.request.AuthenticationRequest;
import com.example.ap_ment.dto.request.RegisterRequest;
import com.example.ap_ment.dto.response.AuthenticationResponse;
import com.example.ap_ment.entity.User;
import com.example.ap_ment.entity.UserDetailsImpl;
import com.example.ap_ment.exception.BadRequestException;
import com.example.ap_ment.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @Mock
    JwtService jwtService;
    @Mock
    UserDetailServiceImpl userDetailService;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserService userService;
    @InjectMocks
    private AuthenticationService authenticationService;
    private static RegisterRequest rr;
    private static AuthenticationRequest ar;

    @BeforeAll
    static void setup(){
        rr = new RegisterRequest(
                "sandriy@gmail.com",
                "1234567",
                "Andrii",
                "Snovyda");
        ar = new AuthenticationRequest(
                "sandriy@gmail.com",
                "password");
    }

    @Test
    void shouldThrowWhenEmailExists(){
        when(userService.existsByEmail(rr.getEmail())).thenReturn(true);
        assertThatThrownBy(()->authenticationService.signUp(rr))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("The email is already used");
    }
    @Test
    void shouldInvokeMethodSave(){
        when(userService.existsByEmail(rr.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(rr.getPassword())).thenReturn("password");

        authenticationService.signUp(rr);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userService).save(captor.capture());
        User user = captor.getValue();
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getRole()).isEqualTo("USER");
    }

    @Test
    void shouldThrowWhenGoogleUserTriedToSignInWithoutGoogle() {
        User googleUser = new User();
        googleUser.setSignUpByGoogle(true);

        when(userService.existsByEmail(ar.getEmail())).thenReturn(true);
        when(userService.findByEmail(ar.getEmail())).thenReturn(googleUser);

        assertThatThrownBy(()->authenticationService.signIn(ar))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("You signed up using Google. " +
                        "Please sign in using google");
    }
    @Test
    void shouldThrowWhenPasswordIsWrong(){
        User user = new User();
        user.setSignUpByGoogle(false);

        when(userService.existsByEmail(ar.getEmail())).thenReturn(true);
        when(userService.findByEmail(ar.getEmail())).thenReturn(user);
        when(authenticationManager.authenticate(Mockito.any())).thenThrow(BadCredentialsException.class);

        assertThatThrownBy(()->authenticationService.signIn(ar))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("Password is wrong");
    }
    @Test
    void shouldThrowWhenUserDoesntExist(){
        when(userService.existsByEmail(rr.getEmail())).thenReturn(false);
        assertThatThrownBy(()->authenticationService.signIn(ar))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("There is no user with this email. Please sign up");

    }
    @Test
    void shouldReturnJwtToken(){
        User user = User.builder()
                .firstName("Andrii")
                .lastName("Snovyda")
                .email("sandriy@gmail.com")
                .password("password")
                .isSignUpByGoogle(false)
                .build();
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        when(userService.existsByEmail(ar.getEmail())).thenReturn(true);
        when(userService.findByEmail(ar.getEmail())).thenReturn(user);
        when(userDetailService.loadUserByUsername(ar.getEmail())).thenReturn(userDetails);

        assertThat(authenticationService.signIn(ar)).isInstanceOf(AuthenticationResponse.class);
        verify(jwtService).generateToken(userDetails);
    }
}