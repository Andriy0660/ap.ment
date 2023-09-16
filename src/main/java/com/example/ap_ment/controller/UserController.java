package com.example.ap_ment.controller;

import com.example.ap_ment.entity.User;
import com.example.ap_ment.entity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("apment/v1/user")
@RequiredArgsConstructor
public class UserController {
    @GetMapping
    public ResponseEntity<String> getUser(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        return ResponseEntity.ok(user.getFirstName());
    }
    @GetMapping("/email")
    public ResponseEntity<String> getEmail(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        return ResponseEntity.ok(user.getEmail());
    }
}
