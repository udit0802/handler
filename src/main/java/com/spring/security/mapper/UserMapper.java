/**
 * 
 */
package com.spring.security.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.spring.security.entity.User;


/**
 * @author B0073698
 *
 */
public class UserMapper implements RowMapper<User> {

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		ResultSetMetaData rsMetaData = rs.getMetaData();
		int numberOfColumns = rsMetaData.getColumnCount();
		User addedby = new User();
		
		// get the column names; column indexes start from 1
		for (int i = 1; i < numberOfColumns + 1; i++) {
			String columnName = rsMetaData.getColumnName(i);
			// Get the name of the column's table name
			if ("olmid".equalsIgnoreCase(columnName)) {
				user.setOlmId(rs.getString(columnName));
			}else if ("name".equalsIgnoreCase(columnName)) {
				user.setName(rs.getString(columnName));
			}else if ("mobile".equalsIgnoreCase(columnName)) {
				user.setMobile(rs.getString(columnName));
			}else if ("email".equalsIgnoreCase(columnName)) {
				user.setEmail(rs.getString(columnName));
			}else if ("role".equalsIgnoreCase(columnName)) {
				user.setRole(rs.getString(columnName));
			}else if ("password".equalsIgnoreCase(columnName)) {
				user.setPassword(rs.getString(columnName));
			}else if ("addedBy".equalsIgnoreCase(columnName)) {
				addedby.setOlmId(rs.getString(columnName));
				user.setAddedBy(addedby);
			}
		}
		return user;
	}

}
