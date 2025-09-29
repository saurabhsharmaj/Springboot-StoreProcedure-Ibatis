package com.bit.sp.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bit.sp.dto.UserDto;
import com.bit.sp.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/**
 * Service class for user-related business logic.
 * Provides methods to fetch users by status and date using SQL or stored procedures.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    /**
     * Retrieves users by status and date range using a standard SQL query.
     *
     * @param status the user status to filter
     * @param start  the start date
     * @param end    the end date
     * @return list of UserDto objects matching the criteria
     */
    public List<UserDto> getUsersByStatusAndDatesNonSp(String status, Date start, Date end) {
    	log.info("getUsersByStatusAndDatesNonSp Fetching users with status: {}, from: {} to: {}", status, start, end);
    	return userMapper.getUsersByStatusAndDatesNonSp(status, start, end);
    }
    
    /**
     * Retrieves users by status and date range using a stored procedure.
     *
     * @param status the user status to filter
     * @param start  the start date
     * @param end    the end date
     * @return list of UserDto objects returned by the stored procedure
     */
    public List<UserDto> getUsersByStatusAndDatesSp(String status, Date start, Date end) {
    	 log.info("getUsersByStatusAndDatesSp Fetching users with status: {}, from: {} to: {}", status, start, end);
    	Map<String, Object> params = new HashMap<>();
        params.put("status", status);
        params.put("startDate", start);
        params.put("endDate", end);
        
        userMapper.getUsersByStatusAndDatesSp(params);
        
        @SuppressWarnings("unchecked")
        List<UserDto> result = (List<UserDto>) params.get("result");
       log.info("Result size from procedure: {}" , (result != null ? result.size() : 0));
        return result != null ? result : new ArrayList<>();
    }

}
