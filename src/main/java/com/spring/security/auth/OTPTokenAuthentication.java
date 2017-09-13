package com.spring.security.auth;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import com.spring.security.business.UserService;
import com.spring.security.entity.JwtUtil;
import com.spring.security.entity.Status;
import com.spring.security.entity.Token;
import com.spring.security.entity.User;
import com.spring.security.exception.ApplicationException;

@Component
public class OTPTokenAuthentication {

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	UserService userService;

	// @Autowired
	// protected IUserAccessDAO userAccessDAO;
	
	public void manageTokenOnGenerationStore(String olmId,String token,User user) {

		try{
			userService.deletePreviousTokenStoreManager(olmId);
			//saving token in db
			userService.saveTokenStoreManager(olmId, token,user);
			
		} catch (Exception e) {
		}
	
		
	}


//	public void manageTokenOnGenerationStore(String olmId,String token,User user) {
//
//		try{
//			userService.deletePreviousTokenStoreManager(olmId);
//			//saving token in db
//			userService.saveTokenStoreManager(olmId, token,user);
//			
//		} catch (Exception e) {
//			logger.error("Error occurred while creating JWT User Token" + e.getMessage());
//		}
//	
//		
//	}
	
	public UsernamePasswordAuthenticationToken getAuthenticationStore(String token,String userIdentifier) {
		String methodName="getAuthenticationStore";
		UsernamePasswordAuthenticationToken authentication = null;

		try {
			//check if token exists in database
			Token tokenUserDetails = userService.getTokenUserDetailsStoreManager(token,userIdentifier);

			if (null == tokenUserDetails) {
				
				return null;
			}
			
			long tokenValidupto = tokenUserDetails.getValidupto();
			Date date = new Date();
			long currentTime=date.getTime();
			
			if(tokenValidupto<currentTime){
				userService.deleteToken(tokenUserDetails);
				throw new ApplicationException(new Status(500, "Token Expired"), "Token Expired !!!");
				
			}
			
			//retrieve storeCode corrosponding to this token from table and put it into authentication object
//			String storeCode=userService.getManagersDetails(tokenUserDetails.getPhoneNo());
			String storeCode="STORE-1";
			
			
			List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList("ROLE_STOREMANAGER");
			
			//set authentication object
			authentication = new UsernamePasswordAuthenticationToken(tokenUserDetails.getPhoneNo(),storeCode,authorityList);

		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return authentication;
	}
	
	public void manageTokenOnGenerationMerchandiser(String phoneNo,String token) {

		try{
			userService.deletePreviousTokenMerchandiser(phoneNo);
			//saving token in db
			userService.saveTokenStoreMerchandiser(phoneNo, token);
			
		} catch (Exception e) {
		}
	
		
	}
	
	public UsernamePasswordAuthenticationToken getAuthenticationMerchandiser(String token)throws ApplicationException {
		String methodName="getAuthenticationMerchandiser";
		UsernamePasswordAuthenticationToken authentication = null;

		//try {
			//check if token exists in database
			Token tokenUserDetails = userService.getTokenUserDetailsMerchandiser(token);

			if (null == tokenUserDetails) {
//				logger.info(
//						"User not found for this token id in DB, token maybe manipulated");
				return null;
			}
			
			long tokenValidupto = tokenUserDetails.getValidupto();
			Date date = new Date();
//			logger.debug("Current date :: "+date);
			long currentTime=date.getTime();
			
			if(tokenValidupto<currentTime){
//				logger.debug("Token Expired !!!");
				userService.deleteToken(tokenUserDetails);
				throw new ApplicationException(new Status(500, "Token Expired"), "Token Expired !!!");
				
			}
			
			List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList("ROLE_MERCHANDISER");
			
			//set authentication object
			authentication = new UsernamePasswordAuthenticationToken(tokenUserDetails.getPhoneNo(),null,authorityList);

//			logger.debug("User " + tokenUserDetails.getPhoneNo() + " authenticated");
		//} catch (Exception exception) {
			//logger.error("Error occuured while authenticating user " + exception.getMessage());
		//}

		return authentication;
	}

}
