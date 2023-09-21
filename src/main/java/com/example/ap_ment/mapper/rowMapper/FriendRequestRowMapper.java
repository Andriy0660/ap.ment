package com.example.ap_ment.mapper.rowMapper;

import com.example.ap_ment.entity.FriendRequest;
import com.example.ap_ment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
@Component
@RequiredArgsConstructor
public class FriendRequestRowMapper implements RowMapper<FriendRequest> {
    private final UserService userService;
    @Override
    public FriendRequest mapRow(ResultSet resultSet, int i) throws SQLException {
        return FriendRequest.builder()
                .id(resultSet.getInt("id"))
                .sender(userService.findById(resultSet.getInt("sender_id")))
                .receiverId(resultSet.getInt("receiver_id"))
                .build();

    }
}
