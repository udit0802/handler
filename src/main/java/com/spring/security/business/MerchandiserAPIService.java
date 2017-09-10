package com.spring.security.business;

import java.util.List;

import com.spring.security.exception.ApplicationException;

public interface MerchandiserAPIService {
  
  public String retrieveMerchandiserName(String phoneno) throws ApplicationException;
  
  
}
