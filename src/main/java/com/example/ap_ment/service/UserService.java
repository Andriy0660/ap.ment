package com.example.ap_ment.service;

import com.example.ap_ment.dto.response.UserDTO;
import com.example.ap_ment.entity.User;

import java.util.Set;

public interface UserService {
    UserDTO findByFriendRequestCode(String code, Integer userId);
}
