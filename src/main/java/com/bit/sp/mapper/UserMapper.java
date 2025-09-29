package com.bit.sp.mapper;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

import com.bit.sp.dto.UserDto;

/**
 * MyBatis mapper interface for user-related database operations.
 * Provides methods for querying users by status and date, and for calling stored procedures.
 */
@Mapper
public interface UserMapper {

    /**
     * Retrieves users by status and date range using a standard SQL query.
     *
     * @param status    the user status to filter
     * @param startDate the start date
     * @param endDate   the end date
     * @return list of UserDto objects matching the criteria
     */
	@Select("SELECT " +
	        "ID, " +
	        "USERNAME, " +
	        "EMAIL, " +
	        "STATUS, " +
	        "START_DATE, " +
	        "END_DATE " +
	        "FROM users " +
	        "WHERE STATUS = #{status} " +
	        "AND START_DATE >= #{startDate} " +
	        "AND (END_DATE <= #{endDate} OR END_DATE IS NULL) " +
	        "ORDER BY START_DATE")
	@Results({
	    @Result(property = "id", column = "ID", id = true),
	    @Result(property = "username", column = "USERNAME"),
	    @Result(property = "status", column = "STATUS"),
	    @Result(property = "email", column = "EMAIL"),
	    @Result(property = "startDate", column = "START_DATE"),  // Map to startDate field
	    @Result(property = "endDate", column = "END_DATE")       // Map to endDate field
	})
	List<UserDto> getUsersByStatusAndDatesNonSp(@Param("status") String status, 
	                                           @Param("startDate") Date startDate,
	                                           @Param("endDate") Date endDate);

    /**
     * Calls the stored procedure to retrieve users by status and date range.
     * The result is placed in the params map under the key "result".
     *
     * @param params a map containing input parameters and the output result list
     */
	@Select("{CALL GET_USERS_BY_STATUS_AND_DATES("
				+  "#{status, mode=IN, jdbcType=VARCHAR}, "
				+ "#{startDate, mode=IN, jdbcType=DATE}, " 
				+ "#{endDate, mode=IN, jdbcType=DATE}, "
				+ "#{result, mode=OUT, jdbcType=CURSOR, javaType=java.sql.ResultSet, resultMap=userResultMap}" 
			+ ")}")
	@Options(statementType = StatementType.CALLABLE)
	void getUsersByStatusAndDatesSp(Map<String, Object> params);

    /**
     * Provides a result map for mapping user fields from the database.
     * Used internally by MyBatis for mapping stored procedure results.
     *
     * @return a UserDto object (dummy query, not used directly)
     */
	@Select("SELECT * FROM users WHERE 1=0") // Dummy query
	@Results(id = "userResultMap", value = { 
			@Result(property = "id", column = "ID", id = true),
			@Result(property = "username", column = "USERNAME"),
			@Result(property = "status", column = "STATUS"),
			@Result(property = "email", column = "EMAIL"),
			@Result(property = "createdDate", column = "CREATED_DATE") 
	})
	UserDto getUserResultMap();
}
