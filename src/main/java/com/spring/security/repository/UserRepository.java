/**
 * 
 */
package com.spring.security.repository;

import com.spring.security.entity.Merchandiser;
import com.spring.security.entity.Token;
import com.spring.security.entity.User;
import com.spring.security.exception.ApplicationException;

public interface UserRepository {

	public User getUserByOlmId(String olmId) throws ApplicationException;
//	public User getRole(String olmId) throws ApplicationException;
//	public Agency getAgencyDetail(String phoneNo) throws ApplicationException;
//	public void deletePreviousToken(JWTUser user,String token) throws ApplicationException;
//	public void saveToken(JWTUser user,String token) throws ApplicationException;
//	public Token retrieveUserAccess(JWTUser user,String token) throws ApplicationException;
//	public String saveOTP(String mobileNo,String otp,String requestToken,long otpGeneartionTime) throws ApplicationException;
//	public Token getTokenDetails(String mobileNo) throws ApplicationException;
	public void deleteToken(Token token) throws ApplicationException;
	public String getManagersDetails(String olmId) throws ApplicationException;
	public void saveTokenStoreManager(String olmId,String token,User user) throws ApplicationException;
	public void deletePreviousTokenStoreManager(String olmId) throws ApplicationException;
	public Token getTokenUserDetailsStoreManager(String token,String userIdentifier) throws ApplicationException;
	public Merchandiser getMerchandiserDetail(String phoneNo) throws ApplicationException;
	public String saveOTPMerchandiser(String mobileNo,String otp,String requestToken,long otpGeneartionTime) throws ApplicationException;
	public Token getTokenUserDetailsMerchandiser(String token) throws ApplicationException;
	public void deletePreviousTokenMerchandiser(String phoneNo) throws ApplicationException;
	public void saveTokenStoreMerchandiser(String phoneNo,String token) throws ApplicationException;
	public void clearOTPOnLogoutMerchandiser(String phoneNo) throws ApplicationException;
//	public void clearOTPOnLogoutMerchandiser(String phoneNo) throws ApplicationException;
//	public int updateForWrongOTPSubmissionAgency(String mobileNo) throws ApplicationException;
	public int updateForWrongOTPSubmissionMerchandiser(String mobileNo) throws ApplicationException;
	public void selectAndDeleteToken(String mobileNo) throws ApplicationException;
	
	
}
