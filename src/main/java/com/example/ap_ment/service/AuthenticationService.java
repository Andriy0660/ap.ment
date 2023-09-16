package com.example.ap_ment.service;

import com.example.ap_ment.dto.request.AuthenticationRequest;
import com.example.ap_ment.dto.request.RegisterRequest;
import com.example.ap_ment.dto.response.AuthenticationResponse;
import com.example.ap_ment.entity.User;
import com.example.ap_ment.entity.UserDetailsImpl;
import com.example.ap_ment.exception.BadRequestException;
import com.example.ap_ment.exception.ServerErrorException;
import com.example.ap_ment.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserDetailServiceImpl userDetailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public void signUp(RegisterRequest request)
    {
        String email = request.getEmail();

        if(userService.existsByEmail(email)){
            throw new BadRequestException("The email is already used");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .friendRequestCode(RandomStringUtils.randomAscii(5))
                .build();
        if(request.getRole()!=null){user.setRole("MANAGER");}
        else{user.setRole("USER");}
        userService.save(user);
    }

    public AuthenticationResponse signIn(AuthenticationRequest request) {
        if(userService.existsByEmail(request.getEmail())){
            User user = userService.findByEmail(request.getEmail());
            if(user.isSignUpByGoogle())throw new BadRequestException("You signed up using Google. " +
                    "Please sign in using google");
        }
        else{
            throw new UnauthorizedException("There is no user with this email. Please sign up");
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword()
                    )
            );
            //without jwt
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException | InternalAuthenticationServiceException e) {
            throw new UnauthorizedException("Password is wrong");
        }

        UserDetailsImpl user = (UserDetailsImpl) userDetailService.loadUserByUsername(request.getEmail());
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public ResponseEntity<AuthenticationResponse> loginByGoogle(String accessToken)
    {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();

        Request requestForUserInfo = new Request.Builder()
                .url("https://www.googleapis.com/oauth2/v3/userinfo")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .get()
                .build();
        try {
            Response response = client.newCall(requestForUserInfo).execute();
            if (response.isSuccessful()) {
                String responseBody =response.body().string();
                response.close();
                JSONObject jsonObject = new JSONObject(responseBody);
                String firstName = jsonObject.getString("given_name");
                String lastName = jsonObject.getString("family_name");
                String email = jsonObject.getString("email");

                if(userService.existsByEmail(email)){                 //authenticate user

                    UserDetailsImpl userDetails = (UserDetailsImpl) userDetailService.loadUserByUsername(email);
                    User user = userDetails.getUser();

                    if(!user.isSignUpByGoogle()) throw new BadRequestException("The email is already used");

                    if(user.getFirstName().equals(firstName)&&user.getLastName().equals(lastName)){
                        return ResponseEntity.ok(new AuthenticationResponse(jwtService.generateToken(userDetails)));
                    }
                    else{
                        throw new ServerErrorException("An error occurred, contact to API developers AZN_Corp");
                    }
                }
                else {                                  //register user
                    User user = User.builder()
                            .firstName(firstName)
                            .lastName(lastName)
                            .password(passwordEncoder.encode(RandomStringUtils.random(15)))
                            .email(email)
                            .isSignUpByGoogle(true)
                            .build();
                    userService.save(user);
                    UserDetailsImpl userDetails = (UserDetailsImpl) userDetailService.loadUserByUsername(email);
                    return ResponseEntity.ok(new AuthenticationResponse(jwtService.generateToken(userDetails)));
                }
            } else {
                System.out.println("ERROR WHILE GET USER INFO: " + response.code() + " " + response.message());
                throw new ServerErrorException("An error occurred, please try another way to register");
            }
        } catch (IOException e) {
            System.out.println("ERROR WHILE GET STRING OF RESPONSE BODY");
            throw new ServerErrorException("An error occurred, please try another way to register");
        }
    }
}