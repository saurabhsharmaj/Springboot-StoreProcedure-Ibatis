package com.bit.sp.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bit.sp.dto.UserDto;
import com.bit.sp.service.UserService;

@RestController
public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/users")
	public List<UserDto> getUsersByStatusAndDates(@RequestParam(required = false) String status,
			@RequestParam String startDate,
			@RequestParam String endDate) {
		if (status.equalsIgnoreCase("active"))
			return userService.getUsersByStatusAndDatesFromProcedure(status, Date.valueOf(startDate), Date.valueOf(endDate));
		else
			return userService.getUsersByStatusAndDates(status, Date.valueOf(startDate), Date.valueOf(endDate));
	}
}
