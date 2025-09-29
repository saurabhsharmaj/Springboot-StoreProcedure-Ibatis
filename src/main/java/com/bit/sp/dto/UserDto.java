package com.bit.sp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Data Transfer Object representing a user entity.
 * Contains user details such as ID, username, status, email, and relevant dates.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDto {
    /** User ID */
    private Long id;
    /** Username of the user */
    private String username;
    /** Status of the user (e.g., active, inactive) */
    private String status;
    /** Email address of the user */
    private String email;
    /** Date the user was created */
    private java.util.Date createdDate;
    /** Start date for the user (e.g., active period) */
    private java.util.Date startDate;
    /** End date for the user (e.g., active period) */
    private java.util.Date endDate;
}
