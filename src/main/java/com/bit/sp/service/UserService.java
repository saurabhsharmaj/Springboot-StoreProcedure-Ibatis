package com.bit.sp.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bit.sp.dto.UserDto;
import com.bit.sp.mapper.UserMapper;
@Service
public class UserService {

	@Autowired
    private UserMapper userMapper;
    
    public List<UserDto> getUsersByStatusAndDatesNonSp(String status, Date start, Date end) {
        return userMapper.getUsersByStatusAndDatesNonSp(status, start, end);
    }
    
    public List<UserDto> getUsersByStatusAndDatesSp(String status, Date start, Date end) {
        Map<String, Object> params = new HashMap<>();
        params.put("status", status);
        params.put("startDate", start);
        params.put("endDate", end);
        
        userMapper.getUsersByStatusAndDatesSp(params);
        
        @SuppressWarnings("unchecked")
        List<UserDto> result = (List<UserDto>) params.get("result");
        System.out.println("Result size from procedure: " + (result != null ? result.size() : 0));
        return result != null ? result : new ArrayList<>();
    }

}
	