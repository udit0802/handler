package com.spring.security.entity;

public class TokenResponseForOTP {

	private String mobileNo;
	private String requestId;
	
	public TokenResponseForOTP(){
		
	}

	public TokenResponseForOTP(String mobileNo, String requestId) {
		super();
		this.mobileNo = mobileNo;
		this.requestId = requestId;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	
	
}
