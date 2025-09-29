package com.bit.sp.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.bit.sp.dto.UserDto;

@Mapper
public interface UserMapper {
   
    public List<UserDto> getUsersByStatusAndDates(Map<String, Object> params);
}
