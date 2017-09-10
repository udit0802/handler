package com.spring.security.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

import com.spring.security.entity.Merchandiser;


public class MerchandiserResponseMapper implements RowMapper<Merchandiser>{

	@Override
	public Merchandiser mapRow(ResultSet rs, int rowNum) throws SQLException {
	Merchandiser response = new Merchandiser();
	ResultSetMetaData rsMetaData = rs.getMetaData();
	int numberOfColumns = rsMetaData.getColumnCount();
	// get the column names; column indexes start from 1
			for (int i = 1; i < numberOfColumns + 1; i++) {
				String columnName = rsMetaData.getColumnName(i);
				// Get the name of the column's table name
				if ("name".equalsIgnoreCase(columnName)) {
					response.setName(rs.getString(columnName));
				}else if ("phoneNo".equalsIgnoreCase(columnName)) {
					response.setPhoneNo(rs.getString(columnName));
				}else if ("otp".equalsIgnoreCase(columnName)) {
					response.setOtp(rs.getInt(columnName));
				}else if ("otpgenerationtime".equalsIgnoreCase(columnName)) {
					response.setOtpgenerationtime(rs.getLong(columnName));
				}else if ("repetitioncount".equalsIgnoreCase(columnName)) {
					response.setRepetitioncount(rs.getInt(columnName));
				}else if ("requestID".equalsIgnoreCase(columnName)) {
					response.setRequestID(rs.getString(columnName));
				}else if ("role".equalsIgnoreCase(columnName)) {
					response.setRole(rs.getString(columnName));
				}else if ("wrongSubmissionCount".equalsIgnoreCase(columnName)) {
					response.setWrongSubmissionCount(rs.getInt(columnName));
				}else if ("lastUpdationTime".equalsIgnoreCase(columnName)) {
					response.setLastUpdationTime(rs.getLong(columnName));
				}else if ("agencyName".equalsIgnoreCase(columnName)) {
					response.setAgencyName(rs.getString(columnName));
				};
	}
			return response;
}
}

