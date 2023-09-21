package com.example.ap_ment.mapper.rowMapper;

import com.example.ap_ment.entity.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("id"))
                .email(resultSet.getString("email"))
                .firstName(resultSet.getString("first_name"))
                .lastName(resultSet.getString("last_name"))
                .password(resultSet.getString("password"))
                .role(resultSet.getString("role"))
                .isSignUpByGoogle(resultSet.getBoolean("is_sign_up_by_google"))
                .friendRequestCode(resultSet.getString("friendrequest_code"))
                .build();

    }
}
