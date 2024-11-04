package com.jwt.demo.jwt;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
//AccessDeniedHandler는 Spring Security에서 인증된 사용자가 권한이 부족하여 요청한 리소스에 접근할 수 없을 때 처리하는 인터페이스입니다. 
//주로 특정 역할이 없는 사용자가 리소스를 요청할 때 발생하는 AccessDeniedException을 처리하는 데 사용됩니다.
//AccessDeniedHandler를 구현하여 커스텀한 접근 거부 처리 로직을 추가할 수 있습니다.
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
}