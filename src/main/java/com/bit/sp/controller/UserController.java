package com.bit.sp.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bit.sp.dto.UserDto;
import com.bit.sp.mapper.UserMapper;
import com.bit.sp.service.UserService;
import com.bit.sp.utils.ExcelExportUtil;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	private final ExcelExportUtil excelExportUtil;

	
	@GetMapping("/users")
	public List<UserDto> getUsersByStatusAndDates(@RequestParam(required = false) String status,
			@RequestParam String startDate, @RequestParam String endDate) {
		log.info("getUsersByStatusAndDates Fetching users with status: {}, from: {} to: {}", status, startDate, endDate);
		if ("active".equalsIgnoreCase(status)) {
			return userService.getUsersByStatusAndDatesSp(status, Date.valueOf(startDate), Date.valueOf(endDate));
		} else {
			return userService.getUsersByStatusAndDatesNonSp(status, Date.valueOf(startDate), Date.valueOf(endDate));
		}
	}

	@GetMapping("/users/excel")
	public void getUsersByStatusAndDates(@RequestParam(required = false) String status, @RequestParam String startDate,
			@RequestParam String endDate, HttpServletResponse response) throws IOException {
		log.info("Generate Excel >> getUsersByStatusAndDates Fetching users with status: {}, from: {} to: {}", status, startDate, endDate);
		List<UserDto> users = new ArrayList<UserDto>();
		if ("active".equalsIgnoreCase(status)) {
			users= userService.getUsersByStatusAndDatesSp(status, Date.valueOf(startDate), Date.valueOf(endDate));
		} else {
			users= userService.getUsersByStatusAndDatesNonSp(status, Date.valueOf(startDate), Date.valueOf(endDate));
		}

		excelExportUtil.exportToExcel(response, users, "Users_Report");
	}
}
