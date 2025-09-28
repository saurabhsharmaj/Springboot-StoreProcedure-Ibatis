package com.bit.sp.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.bit.sp.dto.UserDto;
import com.bit.sp.mapper.UserMapper;
@Service
public class UserService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private UserMapper userMapper;
    
    public UserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    

    public List<UserDto> getUsersByStatusAndDates(String status, Date start, Date end) {
    	
        String sql = "SELECT id, username, email, status, created_at " +
                     "FROM users " +
                     "WHERE status = ? " +
                     "AND created_at BETWEEN ? AND ?";

        return jdbcTemplate.query(
            sql,
            new Object[]{status, start, end},
            (rs, rowNum) -> {
                UserDto user = new UserDto();
                user.setId(rs.getBigDecimal("id").toBigInteger());
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setStatus(rs.getString("status"));               
                return user;
            }
        );
    }
    
    public List<UserDto> getUsersByStatusAndDatesFromProcedure(String status, Date start, Date end) {
    	return userMapper.getUsersByStatusAndDates(status, start, end);
    }



}
	