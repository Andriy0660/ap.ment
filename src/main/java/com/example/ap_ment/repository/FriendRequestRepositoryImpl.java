package com.example.ap_ment.repository;

import com.example.ap_ment.entity.FriendRequest;
import com.example.ap_ment.exception.BadRequestException;
import com.example.ap_ment.mapper.rowMapper.FriendRequestRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@RequiredArgsConstructor
@Repository
public class FriendRequestRepositoryImpl implements FriendRequestRepository{
    private final JdbcTemplate jdbcTemplate;
    private final FriendRequestRowMapper rowMapper;
    @Override
    public Optional<FriendRequest> findById(Integer id) {
        String sql = "Select * from friend_requests where id = ?";
        return jdbcTemplate.query(sql,rowMapper,id).stream().findFirst();
    }

    @Override
    public FriendRequest save(FriendRequest friendRequest) {
        String sql = "Insert into friend_requests(sender_id,receiver_id)" +
                "values (?,?)";
        jdbcTemplate.update(sql,
                friendRequest.getSender().getId(),friendRequest.getReceiverId());
        String getUser= "select * from friend_requests where sender_id = ? and receiver_id = ?";

        return jdbcTemplate.query(getUser,
                rowMapper, friendRequest.getSender().getId(), friendRequest.getReceiverId())
                .stream().findFirst().orElseThrow(()->
                        new BadRequestException("Server error while finding saved user(save())"));
    }

    @Override
    public int delete(FriendRequest friendRequest) {
        String sql = "delete from friend_requests where id = ?";
        return jdbcTemplate.update(sql,friendRequest.getId());
    }
    @Override
    public int deleteAll() {
        String sql = "delete from friend_requests";
        return jdbcTemplate.update(sql);
    }

    @Override
    public boolean existsBySenderIdAndReceiverId(Integer senderId, Integer receiverId) {
        String sql = "Select * from friend_requests where sender_id = ? and receiver_id=?";
        return jdbcTemplate.query(sql,rowMapper,senderId,receiverId).stream().count()>0;
    }
}
