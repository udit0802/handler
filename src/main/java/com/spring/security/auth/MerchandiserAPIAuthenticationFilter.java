package com.spring.security.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.security.entity.JwtUtil;
import com.spring.security.entity.Status;
import com.spring.security.exception.ApplicationException;

public class MerchandiserAPIAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	OTPTokenAuthentication otpTokenService;


	public String convertObjectToJson(Object object) throws JsonProcessingException {
		if (object == null) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		String methodName = "doFilterInternal for merchandiser";
		logger.debug(methodName + " starts");
//		String version = request.getHeader("version");
		

		String merchandiserToken = request.getHeader("Authorization");

		if (null == merchandiserToken) {
			throw new InsufficientAuthenticationException("Authorization header not found");
		}

		UsernamePasswordAuthenticationToken authentication = null;
		try {
			authentication = otpTokenService.getAuthenticationMerchandiser(merchandiserToken);
			if (null == authentication) {
				logger.debug("invalid token exception");
				throw new ApplicationException(new Status(500, "Invalid Token Exception"), "Invalid token");
			}

		} catch (ApplicationException e) {
			Map<String, Object> session = new HashMap<String, Object>();
			session.put("status", new Status(1212, "Invalid Token Exception"));
			session.put("result", "");
			response.getWriter().write(convertObjectToJson(session));
			response.getWriter().flush();
			response.sendError(0);
		}

		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);
	}

}