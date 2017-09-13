package com.spring.security.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/interceptor")
public class InterceptorController {

	@RequestMapping(value = "/test")
	public String getInterceptorResponse(@RequestParam(value = "info")String info){
		return info + "abc";
	}
}
