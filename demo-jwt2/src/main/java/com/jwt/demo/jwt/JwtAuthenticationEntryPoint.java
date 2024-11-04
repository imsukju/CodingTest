package com.jwt.demo.jwt;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//AuthenticationEntryPoint는 Spring Security에서 인증되지 않은 사용자가 보호된 리소스에 접근하려 할 때 처리하는 인터페이스입니다. 
//보통 로그인하지 않은 사용자가 인증이 필요한 페이지에 접근할 때 작동하여, 로그인 페이지로 리다이렉트하거나, REST API 요청의 경우 적절한 오류 메시지를 응답하는 역할을 합니다.
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}