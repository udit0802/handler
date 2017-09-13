/**
 * 
 */
package com.spring.security.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.spring.security.business.UserService;
import com.spring.security.entity.Merchandiser;
import com.spring.security.entity.Token;
import com.spring.security.entity.User;
import com.spring.security.exception.ApplicationException;
import com.spring.security.repository.UserRepository;


@Component
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	public void saveTokenStoreManager(String olmId,String token,User user) throws ApplicationException{
		userRepository.saveTokenStoreManager(olmId, token,user);
	}
	
	public void deletePreviousTokenStoreManager(String olmId) throws ApplicationException{
		userRepository.deletePreviousTokenStoreManager(olmId);
	}
	
	@Override
	public String getManagersDetails(String olmId) throws ApplicationException{
		return userRepository.getManagersDetails(olmId);
	}
	
	@Override
	public Token getTokenUserDetailsStoreManager(String token,String userIdentifier) throws ApplicationException{
		return userRepository.getTokenUserDetailsStoreManager(token,userIdentifier);
	}
	
	@Override
	public User getUserByOlmId(String olmId) throws ApplicationException {
		return userRepository.getUserByOlmId(olmId);
	}

	@Override
	public Merchandiser getMerchandiserDetail(String phoneNo) throws ApplicationException {
		return userRepository.getMerchandiserDetail(phoneNo);
	}

	@Override
	public String saveOTPMerchandiser(String mobileNo, String otp, String requestToken, long otpGeneartionTime)
			throws ApplicationException {
		return userRepository.saveOTPMerchandiser(mobileNo, otp, requestToken, otpGeneartionTime);
	}

//	@Override
//	public void clearOTPOnLogoutAgency(String phoneNo) throws ApplicationException {
//		userRepository.clearOTPOnLogoutMerchandiser(phoneNo);
//		
//	}
	
	public void selectAndDeleteToken(String mobileNo) throws ApplicationException{
		userRepository.selectAndDeleteToken(mobileNo);
	}
	
	public void saveTokenStoreMerchandiser(String phoneNo,String token) throws ApplicationException{
		userRepository.saveTokenStoreMerchandiser(phoneNo, token);
	}
	
	public int updateForWrongOTPSubmissionMerchandiser(String mobileNo) throws ApplicationException{
		return userRepository.updateForWrongOTPSubmissionMerchandiser(mobileNo);
	}
	
	public void deletePreviousTokenMerchandiser(String phoneNo) throws ApplicationException{
		userRepository.deletePreviousTokenMerchandiser(phoneNo);
	}
	
	public Token getTokenUserDetailsMerchandiser(String token) throws ApplicationException{
		return userRepository.getTokenUserDetailsMerchandiser(token);
	}
	
	public void deleteToken(Token token) throws ApplicationException{
		userRepository.deleteToken(token);
	}
	
	public void clearOTPOnLogoutMerchandiser(String phoneNo) throws ApplicationException{
		userRepository.clearOTPOnLogoutMerchandiser(phoneNo);
	}
}
