package com.example.ap_ment.service;

import com.example.ap_ment.entity.User;
import com.example.ap_ment.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    @Autowired
    private UserRepository repository;
    boolean existsUserByEmail(String email){return repository.existsByEmail(email);}
    User findUserByEmail(String email){return repository.findByEmail(email).orElseThrow(()->
            new UsernameNotFoundException("There is no user with this email"));}
    public User findUserById(Long id){
        Optional<User> optionalUser = repository.findById(id);
        if(optionalUser.isEmpty()){
            throw new UsernameNotFoundException("User with this id is not found");
        }
        return optionalUser.get();
    }
    public void save(User user) {
        repository.save(user);
    }

}
