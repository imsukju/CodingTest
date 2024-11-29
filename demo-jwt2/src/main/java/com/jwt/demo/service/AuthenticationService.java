package com.jwt.demo.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.jsonwebtoken.io.Decoders;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import com.jwt.demo.controller.RefreshTokenRequest;
import com.jwt.demo.controller.TokenResponse;
import com.jwt.demo.dto.LoginDto;
import com.jwt.demo.dto.TokenDto;
import com.jwt.demo.entities.RefreshToken;
import com.jwt.demo.jwt.JwtFilter;
import com.jwt.demo.jwt.TokenProvider;
import com.jwt.demo.repository.RefreshTokenRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationService {
	
	private final TokenProvider tokenProvider;
    @Autowired
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;	
	
    // 토큰을 만드는 로직 메소드
	public Optional<TokenResponse> makeTokens(LoginDto loginDto) {
		log.info("makeTokens");
    	
        UsernamePasswordAuthenticationToken authenticationToken =
        		//UsernamePasswordAuthenticationToken은 Spring Security에서 사용자의 인증(인증 정보와 권한 검증)을 처리하기 위해 사용하는 토큰 객체입니다. 
        		//이 토큰은 사용자 이름과 비밀번호를 이용한 인증을 지원하며, 인증 요청과 인증 결과를 나타내는 데 사용됩니다. 
        		//일반적으로 인증 필터나 AuthenticationManager를 통해 생성, 처리되며, 이를 통해 인증된 사용자의 정보와 권한을 시스템 전반에서 참조할 수 있게 됩니다.
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        log.info("username=" + authentication.getName());
        log.info("authentication = " + authenticationManagerBuilder.getObject().authenticate(authenticationToken));
        //Spring 의 SecurityContextHolder에 SercurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //JWT토큰을 만듬
        String accessToken = tokenProvider.createToken(authentication, true);
        // 리프레쉬 토큰 만듬
        String refreshToken = tokenProvider.createAndPersistRefreshTokenForUser(authentication);
        
        TokenResponse tokenResponse = new TokenResponse(accessToken,
        		refreshToken);
        
        Optional<TokenResponse> optTokenResponse = 
        		Optional.ofNullable(tokenResponse);        
        
        return optTokenResponse;        
	}
	
	@Transactional
	public Optional<TokenDto> makeNewAccessToken(RefreshTokenRequest refreshTokenRequest,
    		Authentication authentication) {
		String refreshTokenValue = refreshTokenRequest.getRefreshToken();		
    	
    	log.info("refreshToken from user. token value=" + refreshTokenValue);
    	
        RefreshToken validRefreshToken = 
        		refreshTokenRepository.findById(refreshTokenValue)
                .orElseThrow(() -> new IllegalStateException("Invalid refresh token"));
        if(authentication == null)
        {
        	authentication = tokenProvider.getAuthentication(refreshTokenValue);

        }
        
        TokenDto tokenDto = null;
        Optional<TokenDto> optTokenDto = null;
        if (isTokenExpired(validRefreshToken)) {
            refreshTokenRepository.delete(validRefreshToken);
            optTokenDto = Optional.ofNullable(tokenDto);
            return optTokenDto;
        }
        
        log.info("refreshToken from database. token value=" + validRefreshToken.getToken());
        
        
        String accessToken = tokenProvider.createToken(authentication, true);
        
        tokenDto = new TokenDto(accessToken);
        optTokenDto = Optional.ofNullable(tokenDto);
        return optTokenDto;
        
	}
	
	public boolean isTokenExpired(RefreshToken refreshToken) {
        return refreshToken.getExpiryDate().isBefore(LocalDateTime.now());
    }
	
	public String LogoutService()
	{
		
		//동기작업이므로 값을 얻어울 수 있음
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		
		if (context != null) 
		{
			SecurityContextHolder.clearContext();
			if (SecurityContextHolder.getContext().getAuthentication() == null) 
			{
				return authentication.getPrincipal() + "님 로그아웃 완료되었습니다";
			}
			else
			{
				return "무언가 잘못되었습니다";
			}
		

		}else
		{
			return "로그인상태인 사용자가 아닙니다";
		}

	}
	
	public Date checkToken(TokenDto token)
	{
		Claims c = tokenProvider.getTokenInfo(token);
		return c.getExpiration();
	}
	
}
