package com.spring.security.repository.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.spring.security.entity.Status;
import com.spring.security.entity.User;
import com.spring.security.exception.ApplicationException;
import com.spring.security.mapper.UserMapper;
import com.spring.security.repository.StoreManagerAPIRepository;

@Component
public class StoreManagerAPIRepositoryImpl implements StoreManagerAPIRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public String retrieveStoreManagerName(String olmId) throws ApplicationException{
		String methodName="retrieveStoreManagerName";

		StringBuilder sb = new StringBuilder("SELECT * FROM vm_user WHERE role=? and UPPER(olmid) = UPPER(?)");
		List<User> users = jdbcTemplate.query(sb.toString(), new String[]{"STOREMANAGER",olmId}, new UserMapper());
		if(users!=null && users.size() >0){
			return users.get(0).getName();
		}else{
			throw new ApplicationException(new Status(500, "User is not authorized as StoreManager"), "User not authorized as Store Manager !!");
		}
	}

	
	
	
}

