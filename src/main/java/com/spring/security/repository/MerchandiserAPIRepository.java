package com.spring.security.repository;

import java.util.List;

import com.spring.security.exception.ApplicationException;

public interface MerchandiserAPIRepository {

	public String retrieveMerchandiserName(String phoneno) throws ApplicationException;
	
	
}
