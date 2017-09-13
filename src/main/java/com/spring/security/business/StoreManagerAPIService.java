package com.spring.security.business;

import com.spring.security.exception.ApplicationException;

public interface StoreManagerAPIService {
  
  public String retrieveStoreManagerName(String olmId) throws ApplicationException;
  
}
