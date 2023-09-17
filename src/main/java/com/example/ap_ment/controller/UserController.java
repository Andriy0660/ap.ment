package com.example.ap_ment.controller;

import com.example.ap_ment.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("apment/v1/user")
@RequiredArgsConstructor
//TEST CONTROLLER
public class UserController {
    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> getUser(){
        User user = (User) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        return ResponseEntity.ok(user.getFirstName());
    }
    @GetMapping("/email")
    public ResponseEntity<String> getEmail(){
        User user = (User) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        return ResponseEntity.ok(user.getEmail());
    }
}
