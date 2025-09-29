package com.bit.sp;

import com.bit.sp.dto.UserDto;
import com.bit.sp.mapper.UserMapper;
import com.bit.sp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUsersByStatusAndDatesNonSp_returnsUsers() {
        List<UserDto> mockList = new ArrayList<>();
        mockList.add(UserDto.builder().id(1L).username("user1").build());
        when(userMapper.getUsersByStatusAndDatesNonSp(eq("active"), any(), any())).thenReturn(mockList);
        List<UserDto> result = userService.getUsersByStatusAndDatesNonSp("active", Date.valueOf("2023-01-01"), Date.valueOf("2023-12-31"));
        assertEquals(1, result.size());
        assertEquals("user1", result.get(0).getUsername());
    }

    @Test
    void testGetUsersByStatusAndDatesNonSp_returnsEmpty() {
        when(userMapper.getUsersByStatusAndDatesNonSp(any(), any(), any())).thenReturn(new ArrayList<>());
        List<UserDto> result = userService.getUsersByStatusAndDatesNonSp("inactive", Date.valueOf("2023-01-01"), Date.valueOf("2023-12-31"));
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetUsersByStatusAndDatesSp_returnsUsers() {
        Map<String, Object> params = new HashMap<>();
        List<UserDto> mockList = new ArrayList<>();
        mockList.add(UserDto.builder().id(2L).username("user2").build());
        params.put("result", mockList);
        doAnswer(invocation -> {
            Map<String, Object> arg = invocation.getArgument(0);
            arg.put("result", mockList);
            return null;
        }).when(userMapper).getUsersByStatusAndDatesSp(any());
        List<UserDto> result = userService.getUsersByStatusAndDatesSp("active", Date.valueOf("2023-01-01"), Date.valueOf("2023-12-31"));
        assertEquals(1, result.size());
        assertEquals("user2", result.get(0).getUsername());
    }

    @Test
    void testGetUsersByStatusAndDatesSp_returnsEmpty() {
        doAnswer(invocation -> {
            Map<String, Object> arg = invocation.getArgument(0);
            arg.put("result", null);
            return null;
        }).when(userMapper).getUsersByStatusAndDatesSp(any());
        List<UserDto> result = userService.getUsersByStatusAndDatesSp("inactive", Date.valueOf("2023-01-01"), Date.valueOf("2023-12-31"));
        assertTrue(result.isEmpty());
    }
}

