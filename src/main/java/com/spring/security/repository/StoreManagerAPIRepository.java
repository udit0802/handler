package com.spring.security.repository;

import com.spring.security.exception.ApplicationException;



public interface StoreManagerAPIRepository {

	
	public String retrieveStoreManagerName(String olmId) throws ApplicationException;
	
}
