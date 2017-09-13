package com.spring.security.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.security.business.UserService;
import com.spring.security.entity.JwtUtil;
import com.spring.security.entity.OTPResponse;
import com.spring.security.entity.ResponseWrapper;
import com.spring.security.entity.TokenMerchandiser;
import com.spring.security.entity.TokenResponseForOTP;
import com.spring.security.exception.ApplicationException;


@RestController
@RequestMapping("/vmMerchandiserAPI")

public class MerchandiserControllerAPI {


//	@Autowired
//	MerchandiserAPIService merchandiserAPIService;

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	UserService userService;

//	@Autowired
//	MerchandiserService merchandiserService;

	/*@Value("${ServerKey}")*/
	private String CLIENT_TOKEN="AAAASQXYY84:APA91bFshcteqRrFnaNeXfuQ02EgwJT-j4IbyXPKFUrM-ffShQ_7WLF1M-mNVTV8HgFFT40Y5cPDPylG9apdewVHLVzL-8aad-OEQn0ETwzSKvJLOLdM3usUvy6VeoPMlgWENOwa7Six";

	/*@Value("${requestURI}")*/
	private String requestURI="http://fcm.googleapis.com/fcm/send";


//	final static Logger logger = Logger.getLogger(MerchandiserControllerAPI.class);
//	final static Logger auditLog = Logger.getLogger("auditLogger");


	@RequestMapping(value = "/getOTP", method = RequestMethod.GET)
	public ResponseWrapper<TokenResponseForOTP> getOTPMerchandiser(
			@RequestParam("mobileNo") String mobileNo) throws ApplicationException{
		String methodName = "getOTPMerchandiser";
		System.out.println(methodName + " starts ");
//		auditLog.debug(methodName+" starts ");
		return ResponseWrapper.getSuccessResponse(jwtUtil.getRequestIdForOTPMerchandiser(mobileNo));
	}

	@RequestMapping(value = "/verifyOTP", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseWrapper<TokenMerchandiser> verifyOTPMerchandiser(@RequestBody OTPResponse user) throws ApplicationException {
		String methodName = "verifyOTPMerchandiser";
//		logger.debug(methodName + " starts ");
//		logger.debug(methodName + " phoneNo "+user.getPhoneNo());
//		logger.debug(methodName + " otp "+user.getOtp());
//		logger.debug(methodName + " reuestId "+user.getRequestId());

//		auditLog.debug(methodName+" starts ");

		return ResponseWrapper.getSuccessResponse(jwtUtil.verifyOTPMerchandiser(user));
	}

	@RequestMapping(value="/logout")
	public @ResponseBody ResponseWrapper<String> logoutMerchandiser(HttpServletRequest request, HttpServletResponse response) throws ApplicationException{
//		String methodName="logout for logoutMerchandiser";
//		logger.debug(methodName+" starts");

//		auditLog.debug(methodName+" starts ");
		//check if that user exists in database

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
//			logger.debug(methodName+"in auth not null");
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		HttpSession session = request.getSession();
		if (session != null) {
			session.invalidate();
		}

		//also delete token from database
//		logger.debug(methodName+" phoneNo "+auth.getPrincipal().toString());
		userService.deletePreviousTokenMerchandiser((String)auth.getPrincipal());

		//clear from data

		userService.clearOTPOnLogoutMerchandiser((String)auth.getPrincipal());

		return ResponseWrapper.getSuccessResponse("Success");
	}


//	@RequestMapping(value="/retrieveStoreList", method = RequestMethod.GET)
//	public @ResponseBody ResponseWrapper<List<StoreListMerchandiserResponse>> retrieveStoreListMerchandiser(Authentication auth) throws ApplicationException{
//		String methodName="retrieveStoreListMerchandiser";
//		logger.debug(methodName+" starts ");
//		logger.debug(methodName+" phoneNo "+auth.getPrincipal());
//		auditLog.debug(methodName+" starts ");
//
//		return ResponseWrapper.getSuccessResponse((merchandiserAPIService.retrieveStoreListMerchandiser((String)auth.getPrincipal())));
//
//	}
//
//	@RequestMapping(value="/retrieveStoreDetails", method = RequestMethod.POST)
//	public @ResponseBody ResponseWrapper<StoreDetailsResponse> retrieveStoreDetails(@RequestBody StoreListMerchandiserResponse response) throws ApplicationException{
//		String methodName = "retrieveStoreDetails";
//		logger.debug(methodName+" starts ");
//		auditLog.debug(methodName+" starts ");
//
//		return ResponseWrapper.getSuccessResponse(merchandiserAPIService.retrieveStoreDetails(response));
//	}
//
//	@RequestMapping(value="/verifyStore", method = RequestMethod.GET)
//	public @ResponseBody ResponseWrapper<VerifyStoreResponse> verifyStore(@RequestParam("latitude") String latitude,@RequestParam("longitude") String longitude,@RequestParam("storeCode") String storeCode,
//			@RequestParam("campaignId") String campaignId,Authentication auth) throws ApplicationException{
//		String methodName = "verifyStore";
//		logger.debug(methodName+" starts ");
//		//logger.debug(methodName+" phoneNo "+auth.getPrincipal());
//
//		auditLog.debug(methodName+" starts ");
//		return ResponseWrapper.getSuccessResponse(merchandiserAPIService.verifyStore(latitude, longitude, storeCode,campaignId,"9999237617"));
////		return ResponseWrapper.getSuccessResponse(merchandiserAPIService.verifyStore(latitude, longitude, storeCode,campaignId,(String)auth.getPrincipal()));
//	}
//
//
//	@RequestMapping(value="/startWorkMerchandiser", method = RequestMethod.GET)
//	public @ResponseBody ResponseWrapper <List<MerchandiserStoreImageResponse>> startWorkMerchandiser(@RequestParam("campaignId") String campaignId, @RequestParam("storeCode") String storeCode,Authentication auth)throws ApplicationException{
//		String methodName = "startWorkMerchandiser";
//		logger.debug(methodName+" starts ");
//		logger.debug(methodName+" phoneNo "+auth.getPrincipal());
//		return ResponseWrapper.getSuccessResponse (merchandiserAPIService.startWorkMerchandiser(campaignId,storeCode,(String)auth.getPrincipal())); 
//	}
//
//
//	@RequestMapping(value="/finishWorkMerchandiser", method = RequestMethod.POST)
//	public @ResponseBody ResponseWrapper<String> finishWorkMerchandiser(@RequestBody List<MerchandiserStoreImageResponse> workToFinish,HttpServletRequest requestReceived)throws ApplicationException{
//		String methodName = "finishWorkMerchandiser";
//		logger.debug(methodName+" starts ");
//		auditLog.debug(methodName+" starts ");
//		Token notificationid=null;
//		String resp=null;
//		try{
//			//String token=requestReceived.getHeader("Authorization");
//			resp="in notification sending process";
//			notificationid=merchandiserAPIService.finishWorkMerchandiser(workToFinish);
//			
//			
//			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//			credentialsProvider.setCredentials(new AuthScope("northproxy.airtelworld.in", 4145),
//					new UsernamePasswordCredentials("B0096708", "m1n2i3a4s5%"));
//
//			HttpClientBuilder clientBuilder = HttpClientBuilder.create();
//
//			clientBuilder.useSystemProperties();
//			clientBuilder.setProxy(new HttpHost("northproxy.airtelworld.in", 4145));
//			clientBuilder.setDefaultCredentialsProvider(credentialsProvider);
//			clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());
//
//			CloseableHttpClient client = clientBuilder.build();
//
//			HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
//			factory.setHttpClient(client);
//
//			
//			JSONObject notificationJson = new JSONObject();
//			notificationJson.put("title", "VM-StoreManager Notification");
//			notificationJson.put("body", "Merchandiser's activity is now available for your validation");
//			JSONObject requestJson = new JSONObject();
//			//"df8ePMOkngQ:APA91bHojAcyhZwqUXf6cEHcAFf1mpPs246QDAL5vwFVQ0AORniOG8EpVIlL0U1g_Tz-kFPIdwwXtIKNuZ6Ko3y72QPyYs71WnD4tQ4yO-zXJpq2cEedF8URztJczRP6SyqLK26sxPgj"
//			requestJson.put("to",notificationid.getNotificationId());
//			requestJson.put("notification", notificationJson);
//
//			logger.debug("requestJson"+requestJson.toString());	
//
//			RestTemplate restTemplate = new RestTemplate();
//			restTemplate.setRequestFactory(factory);
//
//			HttpHeaders headers = new HttpHeaders();
//			headers.add("Authorization", "key="+CLIENT_TOKEN);
//			headers.add("Content-Type","application/json");
//
//
//			logger.debug("requestURI"+requestURI);		
//			logger.debug("CLIENT_TOKEN"+CLIENT_TOKEN);
//			HttpEntity<String> request = new HttpEntity<String>(requestJson.toString(),headers);
//			logger.debug(request.toString());				
//			ResponseEntity<String> response = restTemplate.postForEntity(requestURI, request, String.class);
//			logger.debug("RESPONSE*********"+response.getBody().toString());
//
//			JSONObject responseObj = new JSONObject(response.getBody());
//			if(responseObj.getInt("success") ==1  && responseObj.getInt("failure")== 0 ){
//				return ResponseWrapper.getSuccessResponse(workToFinish.get(0).getCampaignId());		
//			}else{
//				resp=responseObj.toString();
//				throw new Exception();
//			}
//
//		}
//		catch(Exception e){
//			if(resp != null){
//				//throw new ApplicationException(new Status(2001, "Work finished but notification not sent.Please inform your storeManager"),resp);	
//				throw new ApplicationException(new Status(2001, "Work finished !"),resp);	
//			}
//			else{
//				throw new ApplicationException(new Status(500, "Something went wrong"),e);	
//			}
//
//		}
//
//
//	}


//
//	@RequestMapping(value="/finishWorkMerchandiser", method = RequestMethod.POST)
//	public @ResponseBody ResponseWrapper<String> finishWorkMerchandiser(@RequestBody List<MerchandiserStoreImageResponse> workToFinish)throws ApplicationException{
//		String methodName = "finishWorkMerchandiser";
//		logger.debug(methodName+" starts ");
//		auditLog.debug(methodName+" starts ");
//		return ResponseWrapper.getSuccessResponse (merchandiserAPIService.finishWorkMerchandiser(workToFinish));
//	}


//	@RequestMapping(value="/markAsDoneOrUndone", method = RequestMethod.POST)
//	public @ResponseBody ResponseWrapper<String> markAsDoneOrUndone(@RequestBody MerchandiserStoreImageResponse workToFinish)throws ApplicationException{
//		String methodName = "markAsDoneOrUndone";
//		logger.debug(methodName+" starts ");
//		auditLog.debug(methodName+" starts ");
//		return ResponseWrapper.getSuccessResponse(merchandiserAPIService.markAsDoneOrUndone(workToFinish));
//	}
//
//	@RequestMapping(value="/getImage", method = RequestMethod.GET)
//	public @ResponseBody ResponseWrapper<String> getImage(@RequestParam("imageId") String imageId)throws ApplicationException{
//		String methodName = "getImage";
//		logger.debug(methodName+" starts ");
//		return ResponseWrapper.getSuccessResponse(merchandiserAPIService.getImage(imageId));
//	}


	////////////////////////////////////////////////////////////////////////

	@RequestMapping(value="/getInfo", method = RequestMethod.GET)
	public @ResponseBody ResponseWrapper<String> getInfo(@RequestParam("info") String info)throws ApplicationException{
		return ResponseWrapper.getSuccessResponse(info);
	}

}
