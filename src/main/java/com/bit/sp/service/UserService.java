package com.bit.sp.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
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
    
    public List<UserDto> getUsersByStatusAndDatesFromProcedure(String status, LocalDate start, LocalDate end) {

        String cursorName = "user_cursor";

        // 1️⃣ Call the procedure
        jdbcTemplate.execute(
            "CALL public.get_users_by_status_and_dates(?, ?, ?, ?)",
            (PreparedStatementCallback<Void>) ps -> {
                ps.setString(1, status);
                ps.setDate(2, java.sql.Date.valueOf(start));
                ps.setDate(3, java.sql.Date.valueOf(end));
                ps.setString(4, cursorName);
                ps.execute();
                return null;
            }
        );

        // 2️⃣ Fetch all rows from the cursor
        String fetchSql = "FETCH ALL FROM " + cursorName;
        List<UserDto> users = jdbcTemplate.query(fetchSql,
            (rs, rowNum) -> {
                UserDto user = new UserDto();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setStatus(rs.getString("status"));
                return user;
            }
        );

        // 3️⃣ Close the cursor to avoid resource leak
        jdbcTemplate.execute("CLOSE " + cursorName);

        return users;
    }
}
	