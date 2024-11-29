package com.jwt.demo.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtSecurityConfig extends 
	SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
	
    private final TokenProvider tokenProvider;

    @Override
    public void configure(HttpSecurity http) {
    	
    	http.addFilterBefore(
    			//JwtFilter를 UsernamePasswordAuthenticationFilter 뒤쪽에 배치를 한다.
                new JwtFilter(tokenProvider),
                UsernamePasswordAuthenticationFilter.class
        );
    }
}
