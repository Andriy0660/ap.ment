package com.example.ap_ment.service;

import com.example.ap_ment.dto.response.UserDTO;

public interface UserService {
    UserDTO findByFriendRequestCode(String code, Integer userId);
}
