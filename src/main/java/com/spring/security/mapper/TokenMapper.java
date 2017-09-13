package com.spring.security.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

import com.spring.security.entity.Token;

public class TokenMapper implements RowMapper<Token>{
	
	
	public Token mapRow(ResultSet rs, int rowNum) throws SQLException {
	Token response = new Token();
	ResultSetMetaData rsMetaData = rs.getMetaData();
	int numberOfColumns = rsMetaData.getColumnCount();
	// get the column names; column indexes start from 1
			for (int i = 1; i < numberOfColumns + 1; i++) {
				String columnName = rsMetaData.getColumnName(i);
				// Get the name of the column's table name
				if ("mobileNo".equalsIgnoreCase(columnName)) {
					response.setPhoneNo(rs.getString(columnName));
				}else if ("deviceId".equalsIgnoreCase(columnName)) {
					response.setDeviceId(rs.getString(columnName));
				}else if ("token".equalsIgnoreCase(columnName)) {
					response.setToken(rs.getString(columnName));
				}else if ("isEnabled".equalsIgnoreCase(columnName)) {
					response.setIsEnabled(rs.getString(columnName));
				}else if ("validupto".equalsIgnoreCase(columnName)) {
					response.setValidupto(rs.getLong(columnName));
				}else if ("tokengenerationTime".equalsIgnoreCase(columnName)) {
					response.setCreatedon(rs.getLong(columnName));
				}
			}
			return response;
	
	}
}