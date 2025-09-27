package com.bit.sp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bit.sp.dto.UserDto;

@Mapper
public interface UserMapper {
   
    List<UserDto> getUsersByStatusAndDates(
            @Param("status") String status,
            @Param("startDate") java.sql.Date startDate,
            @Param("endDate") java.sql.Date endDate
        );
}
