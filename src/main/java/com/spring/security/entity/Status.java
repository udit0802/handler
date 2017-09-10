/**
 * 
 */
package com.spring.security.entity;

/**
 * @author Shashank Gheek
 *
 */
public class Status {

	private Integer code;
	private String message;
	
	public Status() {
	}
	
	public Status(Integer code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	/**
	 * @return the code
	 */
	public Integer getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(Integer code) {
		this.code = code;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
