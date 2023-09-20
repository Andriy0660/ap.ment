package com.example.ap_ment.mapper;

import com.example.ap_ment.dto.response.UserDTO;
import com.example.ap_ment.entity.FriendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FriendRequestMapper implements IMapper{
    private final UserMapper userMapper;
    public UserDTO mapRequest(FriendRequest friendRequest){
        return userMapper.map(friendRequest.getSender());
    }
    public Set<UserDTO> mapRequest(Set<FriendRequest> requests){
        return requests.stream().map(this::mapRequest)
                .collect(Collectors.toSet());
    }

    public <V, T> T map(V source) {
        if(source instanceof FriendRequest) return (T)mapRequest((FriendRequest) source);
        else return (T)mapRequest((Set<FriendRequest>) source);
    }
    public <V> boolean supports(V source){
        return (source instanceof FriendRequest) ||
                (source instanceof Set && ((Set<?>)source).iterator().next() instanceof FriendRequest);
    }

}
