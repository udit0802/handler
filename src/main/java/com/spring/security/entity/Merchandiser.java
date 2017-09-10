package com.spring.security.entity;


public class Merchandiser {

	private String name;//
	private String phoneNo;////
	private String agencyName;
	private String agencyCode;
	private int otp;////
	private long otpgenerationtime;////
	private int repetitioncount;////
	private String requestID;////
	private String role;
	private int wrongSubmissionCount;
	private long lastUpdationTime;
	
	public Merchandiser(){
		
	}
	
	


	public Merchandiser(String id, String name, String phoneNo, String agencyName, String agencyCode, int otp,
			long otpgenerationtime, int repetitioncount, String requestID, String role, int wrongSubmissionCount,
			long lastUpdationTime) {
		super();
		this.name = name;
		this.phoneNo = phoneNo;
		this.agencyName = agencyName;
		this.agencyCode = agencyCode;
		this.otp = otp;
		this.otpgenerationtime = otpgenerationtime;
		this.repetitioncount = repetitioncount;
		this.requestID = requestID;
		this.role = role;
		this.wrongSubmissionCount = wrongSubmissionCount;
		this.lastUpdationTime = lastUpdationTime;
	}




	public long getLastUpdationTime() {
		return lastUpdationTime;
	}




	public void setLastUpdationTime(long lastUpdationTime) {
		this.lastUpdationTime = lastUpdationTime;
	}




	public int getWrongSubmissionCount() {
		return wrongSubmissionCount;
	}



	public void setWrongSubmissionCount(int wrongSubmissionCount) {
		this.wrongSubmissionCount = wrongSubmissionCount;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getAgencyName() {
		return agencyName;
	}
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}
	public String getAgencyCode() {
		return agencyCode;
	}
	public void setAgencyCode(String agencyCode) {
		this.agencyCode = agencyCode;
	}







	public int getOtp() {
		return otp;
	}







	public void setOtp(int otp) {
		this.otp = otp;
	}







	public long getOtpgenerationtime() {
		return otpgenerationtime;
	}







	public void setOtpgenerationtime(long otpgenerationtime) {
		this.otpgenerationtime = otpgenerationtime;
	}







	public int getRepetitioncount() {
		return repetitioncount;
	}







	public void setRepetitioncount(int repetitioncount) {
		this.repetitioncount = repetitioncount;
	}







	public String getRequestID() {
		return requestID;
	}







	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}







	public String getRole() {
		return role;
	}







	public void setRole(String role) {
		this.role = role;
	}
	

}
