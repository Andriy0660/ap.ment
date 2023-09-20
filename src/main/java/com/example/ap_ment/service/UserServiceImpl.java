package com.example.ap_ment.service;

import com.example.ap_ment.dto.response.UserDTO;
import com.example.ap_ment.entity.User;
import com.example.ap_ment.exception.BadRequestException;
import com.example.ap_ment.exception.NotFoundException;
import com.example.ap_ment.mapper.MapperManager;
import com.example.ap_ment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final MapperManager mapperManager;
    public void save(User user) {
        repository.save(user);
    }
    public User findById(Integer id){
        Optional<User> optionalUser = repository.findById(id);
        if(optionalUser.isEmpty()){
            throw new UsernameNotFoundException("User with this id is not found");
        }
        return optionalUser.get();
    }
    public User findByEmail(String email){return repository.findByEmail(email).orElseThrow(()->
            new UsernameNotFoundException("There is no user with this email"));}
    boolean existsByEmail(String email){return repository.existsByEmail(email);}
    public UserDTO findByFriendRequestCode(String code, Integer userId){
        User user = repository.findByFriendRequestCode(code).orElseThrow(()->
                new NotFoundException("There is no users with this code"));
        if(user.getId().equals(userId))
            throw new BadRequestException("You can not add yourself to your friends");
        return userToDto(user);
    }
    public UserDTO userToDto(User user){
        return mapperManager.map(user);
    }
}
