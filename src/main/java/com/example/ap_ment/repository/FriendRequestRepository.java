package com.example.ap_ment.repository;

import com.example.ap_ment.entity.FriendRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Integer> {
    @NotNull
    Optional<FriendRequest> findById(@NotNull Integer id);
}
