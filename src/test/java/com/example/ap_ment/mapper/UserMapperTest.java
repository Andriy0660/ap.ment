package com.example.ap_ment.mapper;

import com.example.ap_ment.dto.response.UserDTO;
import com.example.ap_ment.entity.User;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserMapperTest {
    UserMapper userMapper = new UserMapper();

    @Test
    void shouldTransformUserToDTO(){
        User user = User.builder()
                .firstName("Andrii")
                .lastName("Snovyda")
                .email("sandriy@gmail.com")
                .password("1234567")
                .friendRequestCode("HALLO")
                .build();
        UserDTO expected = UserDTO.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
        assertThat(userMapper.userToDTO(user)).isEqualTo(expected);
    }
    @Test
    void shouldTransformSetOfUsersToDTOs(){
        User user1 = User.builder()
                .firstName("Andrii")
                .lastName("Snovyda")
                .email("sandriy@gmail.com")
                .password("1234567")
                .friendRequestCode("HALLO")
                .build();
        UserDTO dto1 = UserDTO.builder()
                .email(user1.getEmail())
                .firstName(user1.getFirstName())
                .lastName(user1.getLastName())
                .build();

        User user2 = User.builder()
                .firstName("Zakhar")
                .lastName("Rapipca")
                .email("zakhar@gmail.com")
                .password("123456")
                .friendRequestCode("HELLO")
                .build();
        UserDTO dto2 = UserDTO.builder()
                .email(user2.getEmail())
                .firstName(user2.getFirstName())
                .lastName(user2.getLastName())
                .build();
        Set<User> users = new HashSet<>();
        users.add(user1);
        users.add(user2);

        Set<UserDTO> expected = new HashSet<>();
        expected.add(dto1);
        expected.add(dto2);

        assertThat(userMapper.setToDTOs(users)).isEqualTo(expected);

    }


}