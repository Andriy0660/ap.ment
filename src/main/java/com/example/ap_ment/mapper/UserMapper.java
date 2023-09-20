package com.example.ap_ment.mapper;

import com.example.ap_ment.dto.response.UserDTO;
import com.example.ap_ment.entity.User;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.stream.Collectors;
@Component
public class UserMapper implements IMapper{
    public UserDTO mapUser(User user){
        return UserDTO.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
    public Set<UserDTO> mapUser(Set<User> users){
        return users.stream().map(this::mapUser)
                .collect(Collectors.toSet());
    }

    public <V, T> T map(V source) {
        if(source instanceof User) return (T)mapUser((User)source);
        else return (T)mapUser((Set<User>) source);
    }
    public <V> boolean supports(V source){
        return (source instanceof User) ||
                (source instanceof Set && ((Set<?>)source).iterator().next() instanceof User);
    }
}
