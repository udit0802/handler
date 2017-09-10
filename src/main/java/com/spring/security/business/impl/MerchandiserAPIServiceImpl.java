package com.spring.security.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.spring.security.business.MerchandiserAPIService;
import com.spring.security.exception.ApplicationException;
import com.spring.security.repository.MerchandiserAPIRepository;


@Component
public class MerchandiserAPIServiceImpl implements MerchandiserAPIService{

	@Autowired
	MerchandiserAPIRepository merchandiserAPIRepository;
	
	public String retrieveMerchandiserName(String phoneno) throws ApplicationException{
		return merchandiserAPIRepository.retrieveMerchandiserName(phoneno);
	}
	
}
