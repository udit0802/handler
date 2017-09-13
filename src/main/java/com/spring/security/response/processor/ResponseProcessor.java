package com.spring.security.response.processor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ResponseProcessor {

	public Object processResponse(Object body, HttpServletRequest request,
			HttpServletResponse response);
}
