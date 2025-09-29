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

/**
 * REST controller for user-related operations such as fetching users and exporting user data to Excel.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	private final ExcelExportUtil excelExportUtil;

	/**
	 * Retrieves a list of users filtered by status and date range.
	 * If status is 'active', uses stored procedure; otherwise, uses a standard query.
	 *
	 * @param status    the user status to filter (optional)
	 * @param startDate the start date (yyyy-MM-dd)
	 * @param endDate   the end date (yyyy-MM-dd)
	 * @return list of UserDto objects matching the criteria
	 */
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

	/**
	 * Exports a list of users filtered by status and date range to an Excel file.
	 * If status is 'active', uses stored procedure; otherwise, uses a standard query.
	 *
	 * @param status    the user status to filter (optional)
	 * @param startDate the start date (yyyy-MM-dd)
	 * @param endDate   the end date (yyyy-MM-dd)
	 * @param response  the HTTP response to write the Excel file to
	 * @throws IOException if an I/O error occurs during export
	 */
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
