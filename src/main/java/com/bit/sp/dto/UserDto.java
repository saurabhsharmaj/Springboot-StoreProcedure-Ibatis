package com.bit.sp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDto {
	private Long id;
	private String username;
	private String status;
	private String email;
	private java.util.Date createdDate;
	private java.util.Date startDate;
	private java.util.Date endDate;
}
