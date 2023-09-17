package com.example.ap_ment.controller;

import com.example.ap_ment.dto.request.AuthenticationRequest;
import com.example.ap_ment.dto.request.RegisterRequest;
import com.example.ap_ment.dto.response.AuthenticationResponse;
import com.example.ap_ment.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("apment/v1/auth")
@RequiredArgsConstructor

public class AuthenticationController {
    private final AuthenticationService authService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(
            @RequestBody @Valid RegisterRequest request
    ){
        authService.signUp(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> signIn(
            @RequestBody @Valid AuthenticationRequest request
    ){
        return ResponseEntity.ok(authService.signIn(request));
    }

//    @GetMapping ("/loginbygoogle")
//    public ResponseEntity<AuthenticationResponse> loginByGoogle(@RequestParam("code") String accessToken) {
//        return authService.loginByGoogle(accessToken);
//    }


//    @GetMapping("/info")
//    public String getInfo(){
//        return "info";
//    }
//    @PreAuthorize("hasRole('MANAGER')")
//    @GetMapping("/biginfo")
//    public String getBigInfo(){
//        return "biginfo";
//    }

}