package com.spring.security.entity;

public class Token {
	
	private String mobileNo;
	private String phoneNo;
	private String deviceId;
	private String token;
	private String isEnabled;
	private long validupto;
	private long createdon;
	private String notificationId;
	public Token(){
		
	}

	public Token(String phoneNo,String mobileNo, String deviceId, String token, String isEnabled, long validupto, long createdon,String notificationId) {
		super();
		this.phoneNo = phoneNo;
		this.deviceId = deviceId;
		this.token = token;
		this.isEnabled = isEnabled;
		this.validupto = validupto;
		this.createdon = createdon;
		this.notificationId=notificationId;
		this.mobileNo=mobileNo;
	}

	
	public String getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}

	public long getValidupto() {
		return validupto;
	}

	public void setValidupto(long validupto) {
		this.validupto = validupto;
	}

	public long getCreatedon() {
		return createdon;
	}

	public void setCreatedon(long createdon) {
		this.createdon = createdon;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	
	
	

	
}
