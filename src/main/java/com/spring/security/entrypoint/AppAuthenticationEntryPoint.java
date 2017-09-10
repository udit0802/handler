package com.spring.security.entrypoint;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

public class AppAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint{

	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	public AppAuthenticationEntryPoint(final String loginFormUrl) {
        super(loginFormUrl);
    }

	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		// redirect to login page. Use https if forceHttps true
		String redirectUrl = buildRedirectUrlToLoginPage(request, response, authException);

		redirectStrategy.sendRedirect(request, response, redirectUrl);
		
		// This is invoked when user tries to access a secured REST resource without supplying any credentials
        // We should just send a 401 Unauthorized response because there is no 'login page' to redirect to
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
	}
	
}
