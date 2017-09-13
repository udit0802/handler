package com.spring.security.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.spring.security.business.StoreManagerAPIService;
import com.spring.security.exception.ApplicationException;
import com.spring.security.repository.StoreManagerAPIRepository;


@Component
public class StoreManagerAPIServiceImpl implements StoreManagerAPIService{

	@Autowired
	StoreManagerAPIRepository storeManagerAPIRepository;
	
	
	public String retrieveStoreManagerName(String olmId) throws ApplicationException{
		String methodName = "retrieveStoreManagerName";
		return storeManagerAPIRepository.retrieveStoreManagerName(olmId);
	}
	
}
