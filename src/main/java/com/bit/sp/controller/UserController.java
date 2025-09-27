package com.bit.sp.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
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
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		if (status.equalsIgnoreCase("active"))
			return userService.getUsersByStatusAndDatesFromProcedure(status, startDate, endDate);
		else
			return userService.getUsersByStatusAndDates(status, startDate, endDate);
	}
}
