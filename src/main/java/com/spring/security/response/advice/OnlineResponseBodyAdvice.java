package com.spring.security.response.advice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.spring.security.response.processor.impl.ModifyHeaderResponseProcessor;

@ControllerAdvice(basePackageClasses={com.spring.security.controller.InterceptorController.class})
public class OnlineResponseBodyAdvice implements ResponseBodyAdvice<Object> {
	
	@Autowired
	private ModifyHeaderResponseProcessor modifyHeaderResponseProcessor;
	
	@Autowired
	private HttpServletRequest httpRequest;
	
	@Autowired
	private HttpServletResponse httpResponse;

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		
		
		Object responseBody = body;
		responseBody = modifyHeaderResponseProcessor.processResponse(body, httpRequest, httpResponse);
		return responseBody;
	}

}
