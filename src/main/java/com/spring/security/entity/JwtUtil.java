package com.spring.security.entity;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.airtel.sms.utils.SMSAdapter;
import com.spring.security.business.MerchandiserAPIService;
import com.spring.security.business.UserService;
import com.spring.security.exception.ApplicationException;

@Component
public class JwtUtil {
    
    @Value("${shortCode}")
    private String shortCode;
    
    @Value("${otp.block.duration}")
    private String otpBlockduration;
    
    @Value("${messageBrokerPassword}")
	 private String messageBrokerPassword;

	 @Value("${messageBrokerUsername}")
	 private String messageBrokerUsername;

	 @Value("${messageBrokerURL}")
	 private String messageBrokerURL;
    
    @Autowired
    UserService userService;
    
    @Autowired
    MerchandiserAPIService merchandiserAPIService;
	
	//get otp for merchandiser
	public TokenResponseForOTP getRequestIdForOTPMerchandiser(String mobileNo) throws ApplicationException{
		String methodName="getRequestIdForOTPMerchandiser";
//		logger.debug(methodName+" starts ");
		TokenResponseForOTP requestId = new TokenResponseForOTP();
		boolean value=false;
		
		 Merchandiser merchandiserDetails = userService.getMerchandiserDetail(mobileNo);
			if(merchandiserDetails == null){
				throw new UsernameNotFoundException("User is not registered by AgencyManager");
			}else if(merchandiserDetails.getRepetitioncount()>=3){
//				logger.debug(methodName+" otp block duration "+otpBlockduration);
				Date date = new Date();
				
//				logger.debug(methodName+" current date"+date);
				
				long timeVlaue=merchandiserDetails.getOtpgenerationtime()+Long.parseLong(otpBlockduration);
				
				Date dateTillBlock = new Date(timeVlaue);
				
//				logger.debug(methodName+" date till blocked "+dateTillBlock);
				
				if(date.getTime()>timeVlaue){
					//clear corrosponding otp details and start inserting new one
					userService.clearOTPOnLogoutAgency(mobileNo);
					
				}else{
				
//					logger.debug("OTP already sent three times !!");
					throw new ApplicationException(new Status(500, "OTP cant be generated more then three times"), "OTP Generation count exceeds !!");
				}
			}
			
			//*Generate token
		String tokenGenerated=UUID.randomUUID().toString();
		
		if(tokenGenerated !=null){
			value=generateOTPMerchandiser(mobileNo,tokenGenerated);
			//comm
			if(!value){
//				logger.debug("Not able to generate pin or send sms");
				throw new ApplicationException(new Status(404, "Not able to send SMS"), "Not able to generate pin or send sms to  :: "+mobileNo);
			}
		}
		
//		logger.debug(methodName+"SMS has been sent !!!");
		requestId.setMobileNo(mobileNo);
		requestId.setRequestId(tokenGenerated);
		
		return requestId;
	}

	

	private String generateOtpOrRequestCode(){
		
		return String.valueOf((int)(Math.random()*9000)+1000);
	}
	
	public boolean generateOTPMerchandiser(String phoneNo,String tokenOTP){
		String methodName="generateOTPMerchandiser";
		boolean val=false;
		try{
			
			String otp=generateOtpOrRequestCode();
			String requestCode=generateOtpOrRequestCode();
			
//			logger.debug(methodName+"otp :: "+otp);
//			logger.debug("requestCode :: "+requestCode);
			
			SMSAdapter adapter = new SMSAdapter();
			String smsText = "Use "+otp+" as one time password (OTP) to login to your account. Do not share this OTP to anyone for security reasons. Valid for 5 minutes";
			val=adapter.sendMessage(phoneNo, shortCode, smsText,messageBrokerURL,messageBrokerUsername,messageBrokerPassword);
//			logger.debug(methodName+"opt sent status:: "+val);
			
			if(val){
//				logger.debug(methodName+"otp sent");
				Date date = new Date();
				long generationTime=date.getTime();
				
				//remove afterward ::: otp
				//check for user and save otp
				userService.saveOTPMerchandiser(phoneNo, otp, tokenOTP, generationTime);
				
		}
			
			return val;
					
			}catch(Exception e){
				e.printStackTrace();
			}
		return val;
	}
	
	public TokenMerchandiser verifyOTPMerchandiser(OTPResponse user)throws ApplicationException{
		String methodName="verifyOTPMerchandiser";
		
		Merchandiser merchandiser = userService.getMerchandiserDetail(user.getPhoneNo());
		if (merchandiser == null) {
			throw new UsernameNotFoundException("User is not registered by Agency");
		}

		if (Integer.parseInt(user.getOtp()) == merchandiser.getOtp()
				&& user.getRequestId().equalsIgnoreCase(merchandiser.getRequestID())) {
			TokenMerchandiser tokenDetails = new TokenMerchandiser();
			String token = UUID.randomUUID().toString();
			tokenDetails.setPhoneNo(user.getPhoneNo());
			tokenDetails.setToken(token);
			
			
			//save token in db and delete if already exists
			userService.selectAndDeleteToken(user.getPhoneNo());
			
			userService.saveTokenStoreMerchandiser(user.getPhoneNo(), token);
			
			///name
			tokenDetails.setName(merchandiserAPIService.retrieveMerchandiserName(user.getPhoneNo()));
			
			return tokenDetails;
		}else{
			//add wrong otp submission
				
				int count=userService.updateForWrongOTPSubmissionMerchandiser(user.getPhoneNo());
				if(count>=3){
					throw new ApplicationException(new Status(500, "You are blocked due to 3 wrong OTP submission.Please try after 5 minutes"), "You are blocked due to 3 wrong OTP submission.Please try after 30 minutes");
				}
				
				throw new ApplicationException(new Status(500, "OTP does not match"), "OTP does not match for  :: "+user.getPhoneNo());
			}
}
	
}
