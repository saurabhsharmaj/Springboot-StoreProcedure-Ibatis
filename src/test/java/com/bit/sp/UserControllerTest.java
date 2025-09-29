package com.bit.sp;

import com.bit.sp.controller.UserController;
import com.bit.sp.dto.UserDto;
import com.bit.sp.service.UserService;
import com.bit.sp.utils.ExcelExportUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {
    @Mock
    private UserService userService;
    @Mock
    private ExcelExportUtil excelExportUtil;
    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUsersByStatusAndDates_active() {
        List<UserDto> mockList = Arrays.asList(UserDto.builder().id(1L).username("user1").build());
        when(userService.getUsersByStatusAndDatesSp(eq("active"), any(), any())).thenReturn(mockList);
        List<UserDto> result = userController.getUsersByStatusAndDates("active", "2023-01-01", "2023-12-31");
        assertEquals(1, result.size());
        assertEquals("user1", result.get(0).getUsername());
    }

    @Test
    void testGetUsersByStatusAndDates_nonActive() {
        List<UserDto> mockList = Arrays.asList(UserDto.builder().id(2L).username("user2").build());
        when(userService.getUsersByStatusAndDatesNonSp(eq("inactive"), any(), any())).thenReturn(mockList);
        List<UserDto> result = userController.getUsersByStatusAndDates("inactive", "2023-01-01", "2023-12-31");
        assertEquals(1, result.size());
        assertEquals("user2", result.get(0).getUsername());
    }

    @Test
    void testGetUsersByStatusAndDates_empty() {
        when(userService.getUsersByStatusAndDatesNonSp(any(), any(), any())).thenReturn(Collections.emptyList());
        List<UserDto> result = userController.getUsersByStatusAndDates("inactive", "2023-01-01", "2023-12-31");
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetUsersByStatusAndDatesExcel_active() throws IOException {
        List<UserDto> mockList = Arrays.asList(UserDto.builder().id(1L).username("user1").build());
        when(userService.getUsersByStatusAndDatesSp(eq("active"), any(), any())).thenReturn(mockList);
        MockHttpServletResponse response = new MockHttpServletResponse();
        doNothing().when(excelExportUtil).exportToExcel(any(), eq(mockList), eq("Users_Report"));
        userController.getUsersByStatusAndDates("active", "2023-01-01", "2023-12-31", response);
        verify(excelExportUtil, times(1)).exportToExcel(any(), eq(mockList), eq("Users_Report"));
    }

    @Test
    void testGetUsersByStatusAndDatesExcel_nonActive() throws IOException {
        List<UserDto> mockList = Arrays.asList(UserDto.builder().id(2L).username("user2").build());
        when(userService.getUsersByStatusAndDatesNonSp(eq("inactive"), any(), any())).thenReturn(mockList);
        MockHttpServletResponse response = new MockHttpServletResponse();
        doNothing().when(excelExportUtil).exportToExcel(any(), eq(mockList), eq("Users_Report"));
        userController.getUsersByStatusAndDates("inactive", "2023-01-01", "2023-12-31", response);
        verify(excelExportUtil, times(1)).exportToExcel(any(), eq(mockList), eq("Users_Report"));
    }

    @Test
    void testGetUsersByStatusAndDatesExcel_empty() throws IOException {
        when(userService.getUsersByStatusAndDatesNonSp(any(), any(), any())).thenReturn(Collections.emptyList());
        MockHttpServletResponse response = new MockHttpServletResponse();
        doNothing().when(excelExportUtil).exportToExcel(any(), eq(Collections.emptyList()), eq("Users_Report"));
        userController.getUsersByStatusAndDates("inactive", "2023-01-01", "2023-12-31", response);
        verify(excelExportUtil, times(1)).exportToExcel(any(), eq(Collections.emptyList()), eq("Users_Report"));
    }
}

