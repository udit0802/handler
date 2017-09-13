package com.spring.security.response.processor.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.spring.security.response.processor.ResponseProcessor;
import com.spring.security.utils.HttpUtils;

@Component
public class ModifyHeaderResponseProcessor implements ResponseProcessor {

	@Override
	public Object processResponse(Object body, HttpServletRequest request,
			HttpServletResponse response) {
		HttpUtils.addNewHeaderHeader(response, body);
		return body;
	}

}
