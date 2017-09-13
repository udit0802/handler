package com.spring.security.utils;

import javax.servlet.http.HttpServletResponse;

public class HttpUtils {
	
	private static String NEW_HEADER = "hello";

	public static void addNewHeaderHeader(HttpServletResponse httpServletResponse, Object body) {
		httpServletResponse.addHeader(NEW_HEADER,  body.toString());
	}
}
