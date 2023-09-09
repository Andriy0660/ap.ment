package com.example.ap_ment.mapper;

import com.example.ap_ment.dto.response.UserDTO;
import com.example.ap_ment.entity.FriendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FriendRequestMapper {
    private final UserMapper userMapper;
    public UserDTO friendRequestToUserDTO(FriendRequest friendRequest){
        return userMapper.userToDTO(friendRequest.getSender());
    }
    public Set<UserDTO> setToDTOs(Set<FriendRequest> requests){
        return requests.stream().map(this::friendRequestToUserDTO)
                .collect(Collectors.toSet());
    }
}
