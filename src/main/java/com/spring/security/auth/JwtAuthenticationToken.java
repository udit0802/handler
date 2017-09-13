package com.spring.security.auth;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken{

	
	private String token;
	
	public JwtAuthenticationToken(String olmId, String token, List<GrantedAuthority> authorities) {
		
		super(olmId, token, authorities);
		this.token = token;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}
	
	
	
}
