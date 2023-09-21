package com.example.ap_ment.repository;


import com.example.ap_ment.entity.User;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByFriendRequestCode(String code);
    boolean existsByEmail(String email);
}
