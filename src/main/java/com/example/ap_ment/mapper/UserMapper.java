package com.example.ap_ment.mapper;

import com.example.ap_ment.dto.response.UserDTO;
import com.example.ap_ment.entity.User;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.stream.Collectors;
@Component
public class UserMapper {
    public UserDTO userToDTO(User user){
        return UserDTO.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
    public Set<UserDTO> setToDTOs(Set<User> users){
        return users.stream().map(this::userToDTO)
                .collect(Collectors.toSet());
    }

}
