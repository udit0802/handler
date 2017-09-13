package com.spring.security.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.security.auth.HRMSAuthentication;
import com.spring.security.auth.OTPTokenAuthentication;
import com.spring.security.business.StoreManagerAPIService;
import com.spring.security.business.UserService;
import com.spring.security.entity.ResponseWrapper;
import com.spring.security.entity.Status;
import com.spring.security.entity.TokenStoreManager;
import com.spring.security.entity.User;
import com.spring.security.exception.ApplicationException;



@RestController
@RequestMapping("/vmStoreManagerAPI")
public class StoreManagerControllerAPI {

	@Autowired
	StoreManagerAPIService StoreManagerAPIService;
//	
	@Autowired
	HRMSAuthentication hrmsAuthentication;
//	
	@Autowired
	UserService userService;
//	
	@Autowired
	OTPTokenAuthentication otpTokenService;
//
//	final static Logger logger = Logger.getLogger(StoreManagerControllerAPI.class);
//	final static Logger auditLog = Logger.getLogger("auditLogger");
//	
//	/*@RequestMapping(value="/sendNotificationIds",method = RequestMethod.POST)
//	public @ResponseBody ResponseWrapper<String> sendNotificationIds(@RequestBody Notification notification) throws ApplicationException{
//		//retrieve notification id and 	device id : @author b0096708
//		return ResponseWrapper.getSuccessResponse(StoreManagerAPIService.setDeviceAndNotificationIDs(notification));
//	}*/
//	
	@RequestMapping(value="/login",method = RequestMethod.POST)
	public @ResponseBody ResponseWrapper<TokenStoreManager> login(@RequestBody User user) throws ApplicationException{
		String methodName="login for storeManager";
		TokenStoreManager tokenResponse = new TokenStoreManager();
		try {
			//check if that user exists in database
			
			User userdetails = userService.getUserByOlmId(user.getOlmId());
			
			if(userdetails.getRole().equalsIgnoreCase("STOREMANAGER")){
				
				hrmsAuthentication.autheticateUser(user.getOlmId(), user.getPassword());
				
				String token = UUID.randomUUID().toString();
				tokenResponse.setStoreCode("STORE-1");
				//* insert into database
				otpTokenService.manageTokenOnGenerationStore(userdetails.getOlmId(),token,user);
				
				tokenResponse.setName(user.getName());
				tokenResponse.setToken(token);
			}else{
				throw new ApplicationException(new Status(500, "User is not authorized as StoreManager"), "User not authorized as Store Manager !!");
			}
			
		} catch (BadCredentialsException e) {
			// TODO Auto-generated catch block
			throw new ApplicationException(new Status(500, "OlmId or password is incorrect"), "OlmId or password is incorrect !!");
			
		}
		return ResponseWrapper.getSuccessResponse(tokenResponse);
	}
//	
//	
//	
	@RequestMapping(value="/logout")
	public @ResponseBody ResponseWrapper<String> logoutStoreManager(HttpServletRequest request, HttpServletResponse response) throws ApplicationException{
		String methodName="logout for storeManager";
			//check if that user exists in database
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null) {
				new SecurityContextLogoutHandler().logout(request, response, auth);
			}
			HttpSession session = request.getSession();
			if (session != null) {
				session.invalidate();
			}
			
			//also delete token from database
			userService.deletePreviousTokenStoreManager((String)auth.getPrincipal());
			
		return ResponseWrapper.getSuccessResponse("Success");
	}
	
	@RequestMapping(value="/info")
	public @ResponseBody ResponseWrapper<String> getInfo(@RequestParam(value="info") String info){
		return ResponseWrapper.getSuccessResponse(info);
	}
	
	
//
//	@RequestMapping(value="/retrieveStoreManagerDashboard", method = RequestMethod.GET)
//	public @ResponseBody ResponseWrapper<List<StoreManagerDashboardResponse>> retrieveStoreManagerDashboard(Authentication auth)throws ApplicationException{
//		String methodName = "retrieveStoreManagerDashboard";
//		logger.debug(methodName+" starts ");
//		logger.debug(methodName+" starts storecode "+auth.getCredentials());
//		auditLog.debug(methodName+" starts ");
//		return ResponseWrapper.getSuccessResponse(StoreManagerAPIService.retrieveStoreManagerDashboard((String)auth.getCredentials()));
//	}
//	
//	@RequestMapping(value="/updateStoreElement", method = RequestMethod.POST)
//	public @ResponseBody ResponseWrapper<String> updateStoreElement(@RequestBody StoreManagerDashboardResponse respObj)throws ApplicationException{
//		String methodName = "updateStoreElement";
//		logger.debug(methodName+" starts ");
//		auditLog.debug(methodName+" starts ");
//		System.out.println(respObj.getRejectionstatus()+"*****************************************************");
//		return ResponseWrapper.getSuccessResponse(StoreManagerAPIService.updateStoreElement(respObj));
//	}
//	
//	
//	@RequestMapping(value="/getSelfAuditResponse", method = RequestMethod.GET)
//	public @ResponseBody ResponseWrapper<List<StoreManagerDashboardResponse>> getSelfAuditResponse(Authentication auth)throws ApplicationException{
//		String methodName = "getSelfAuditResponse";
//		logger.debug(methodName+" starts ");
//		logger.debug(methodName+" starts storecode "+auth.getCredentials());
//		auditLog.debug(methodName+" starts ");
//		return ResponseWrapper.getSuccessResponse(StoreManagerAPIService.getSelfAuditResponse((String)auth.getCredentials()));
//	}
//	
//	@RequestMapping(value="/getImage", method = RequestMethod.GET)
//	public @ResponseBody ResponseWrapper<String> getImage(@RequestParam("imageId") String imageId)throws ApplicationException{
//		String methodName = "getImage";
//		logger.debug(methodName+" starts ");
//		return ResponseWrapper.getSuccessResponse(StoreManagerAPIService.getImage(imageId));
//	}
//	
//	@RequestMapping(value="/getBeatPlan", method = RequestMethod.GET)
//	public @ResponseBody ResponseWrapper<List<BeatPlanStoreResponse>> getBeatPlan(Authentication auth)throws ApplicationException{
//		String methodName = "getBeatPlan";
//		logger.debug(methodName+" starts ");
//		logger.debug(methodName+" starts storecode "+auth.getCredentials());
//		auditLog.debug(methodName+" starts ");
//		return ResponseWrapper.getSuccessResponse(StoreManagerAPIService.getBeatPlan((String)auth.getCredentials()));
//	}
//	
//	@RequestMapping(value="/reschedule", method = RequestMethod.GET)
//	public @ResponseBody ResponseWrapper<String> reschedule(@RequestParam("id") String id)throws ApplicationException{
//		String methodName = "reschedule";
//		logger.debug(methodName+" starts ");
//		auditLog.debug(methodName+" starts ");
//		return ResponseWrapper.getSuccessResponse(StoreManagerAPIService.reschedule(id));
//	}
//	
//	@RequestMapping(value="/getNotificationListForStoreManager",method=RequestMethod.GET)
//	public @ResponseBody ResponseWrapper<List<Notification>> getNotificationListForStoreManager(@RequestParam("id") String id)throws ApplicationException{
//		String methodName = "reschedule";
//		logger.debug(methodName+" starts ");
//		auditLog.debug(methodName+" starts ");
//		return ResponseWrapper.getSuccessResponse(StoreManagerAPIService.getNotificationListForStoreManager(id));
//	}
//	
//	@RequestMapping(value="/setViewedFlag",method=RequestMethod.POST)
//	public @ResponseBody ResponseWrapper<String> setViewedFlag(@RequestBody Notification notificationResponse)throws ApplicationException{
//		String methodName = "setViewedFlag";
//		logger.debug(methodName+" starts ");
//		auditLog.debug(methodName+" starts ");
//		logger.debug(notificationResponse.getId()+"****"+notificationResponse.getStoreManagerid()+"**********"+notificationResponse.getIsviewed());
//		return ResponseWrapper.getSuccessResponse(StoreManagerAPIService.setViewedFlag(notificationResponse));
//	}
}
