package com.spring.security.auth;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.spring.security.stream.HttpServletResponseCopier;

public class ResponseHeaderModificationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
//		response.addHeader("hello", "world");
//		chain.doFilter(request, response);
//		System.out.println(response);
//		response.addHeader("hello", "world");
		
		
		
		
//		if (response.getCharacterEncoding() == null) {
//            response.setCharacterEncoding("UTF-8"); // Or whatever default. UTF-8 is good for World Domination.
//        }
//
//		response.addHeader("hello", "world");
//		PrintWriter out = response.getWriter();
//        HttpServletResponseCopier responseCopier = new HttpServletResponseCopier((HttpServletResponse) response);
//
//        try {
//            chain.doFilter(request, responseCopier);
//            responseCopier.flushBuffer();
//        } finally {
//            byte[] copy = responseCopier.getCopy();
//            String responseStr = new String(copy, response.getCharacterEncoding());
//            System.out.println(responseStr); // Do your logging job here. This is just a basic example.
//            responseCopier.setHeader("resp", responseStr);
//        }
		
		
		
		
//		if (response.getCharacterEncoding() == null) {
//            response.setCharacterEncoding("UTF-8"); // Or whatever default. UTF-8 is good for World Domination.
//        }
//
//        HttpServletResponseCopier responseCopier = new HttpServletResponseCopier((HttpServletResponse) response);
//        responseCopier.flushBuffer();
//        byte[] copy = responseCopier.getCopy();
//        String responseStr = new String(copy, response.getCharacterEncoding());
//        System.out.println(responseStr); // Do your logging job here. This is just a basic example.
//        responseCopier.setHeader("resp", responseStr);
//        chain.doFilter(request, responseCopier);
       
		
		
		
		
		if (response.getCharacterEncoding() == null) {
            response.setCharacterEncoding("UTF-8"); // Or whatever default. UTF-8 is good for World Domination.
        }

//		response.addHeader("hello", "world");
		
        HttpServletResponseCopier responseCopier = new HttpServletResponseCopier((HttpServletResponse) response);

        try {
            chain.doFilter(request, responseCopier);
//            responseCopier.flushBuffer();
        } finally {
            byte[] copy = responseCopier.getCopy();

            String out = new String(copy);
//            out = out + ",hello : " + out;
            
            // DO YOUR REPLACEMENTS HERE
            out = out.replace("abc", "abcd");
            response.getOutputStream().write(out.getBytes());
        }
		
	}

}