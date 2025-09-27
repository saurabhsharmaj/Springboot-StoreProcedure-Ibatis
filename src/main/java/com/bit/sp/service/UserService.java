package com.bit.sp.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.bit.sp.dto.UserDto;
@Service
public class UserService {

    private final JdbcTemplate jdbcTemplate;

    public UserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<UserDto> getUsersByStatusAndDates(String status, LocalDate start, LocalDate end) {
        String sql = "SELECT id, username, email, status, created_at " +
                     "FROM users " +
                     "WHERE status = ? " +
                     "AND created_at BETWEEN ? AND ?";

        return jdbcTemplate.query(
            sql,
            new Object[]{status, java.sql.Date.valueOf(start), java.sql.Date.valueOf(end)},
            (rs, rowNum) -> {
                UserDto user = new UserDto();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setStatus(rs.getString("status"));               
                return user;
            }
        );
    }
}
	