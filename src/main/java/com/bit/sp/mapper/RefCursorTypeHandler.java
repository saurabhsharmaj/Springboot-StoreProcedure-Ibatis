package com.bit.sp.mapper;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;

// This file is no longer needed as MyBatis handles refcursor mapping internally for PostgreSQL.
public class RefCursorTypeHandler extends BaseTypeHandler<ResultSet> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ResultSet parameter, JdbcType jdbcType) {
        // Not needed for OUT parameter
    }

    @Override
    public ResultSet getNullableResult(ResultSet rs, String columnName) {
        return null;
    }

    @Override
    public ResultSet getNullableResult(ResultSet rs, int columnIndex) {
        return null;
    }

    @Override
    public ResultSet getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return (ResultSet) cs.getObject(columnIndex);
    }
}
