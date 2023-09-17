package com.example.ap_ment.mapper;

import com.example.ap_ment.dto.response.UserDTO;
import com.example.ap_ment.entity.FriendRequest;
import com.example.ap_ment.entity.User;
import com.example.ap_ment.exception.BadRequestException;
import com.example.ap_ment.exception.ServerErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class Mapper {
    private final UserMapper userMapper;
    private final FriendRequestMapper friendRequestMapper;
    public <T, V> T map(V source, Class<T>targetType){
        if(!(source instanceof User) && !(source instanceof FriendRequest) && !(source instanceof Set))
            throw new BadRequestException("Unsupported source type");
        if(targetType!=UserDTO.class && targetType!=Set.class){
            throw new BadRequestException("Unsupported target type");
        }

        if(source instanceof User && targetType== UserDTO.class){
            return targetType.cast(userMapper.userToDTO((User)source));
        }
        else if (source instanceof FriendRequest && targetType == UserDTO.class){
            return targetType.cast(friendRequestMapper.friendRequestToUserDTO((FriendRequest) source));
        }

        else if(source instanceof Set<?> && targetType== Set.class){
            if(((Set<?>)source).iterator().next() instanceof User){
                return (T)userMapper.setToDTOs((Set<User>)source);
            }
            else if(((Set<?>)source).iterator().next() instanceof FriendRequest){
                return (T)friendRequestMapper.setToDTOs((Set<FriendRequest>)source);
            }
            else throw new BadRequestException("Unsupported type of Set");
        }

        throw new ServerErrorException("ERROR in Mapper.class");
    }

}
