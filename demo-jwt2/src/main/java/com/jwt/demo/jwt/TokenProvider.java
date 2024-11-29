package com.jwt.demo.jwt;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.jwt.demo.dto.TokenDto;
import com.jwt.demo.entities.RefreshToken;
import com.jwt.demo.repository.RefreshTokenRepository;
import com.jwt.demo.service.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

//import com.jwt.demo.User;
import org.springframework.security.core.userdetails.User;

//빌드 매니저로 인해 검증하고 db에 리프레쉬 토큰을 저장
@Slf4j
@Component
public class TokenProvider implements InitializingBean {

    private static final String AUTHORITIES_KEY = "auth";
    private final String secret;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;
    private Key key;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;


    //Value 값은 application.yml 파일에서 받아옴
   // header: Authorization
   // secret: a2FyaW10b2thcmltdG9rYXJpbXRva2FyaW10b2thcmltdG9rYXJpbXRva2FyaW10b2thcmltdG9rYXJpbXRva2FyaW10b2thcmltdG9rYXJpbXRva2FyaW10b2thcmltdG9rYXJpbXRva2FyaW10b2thcmltdG9rYXJpbXRva2FyaW10b2thcmltdG9rYXJpbQ==
   // token-validity-in-seconds: 200
   // refreshtoken-validity-in-seconds: 1800
    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long accessTokenValidityInSeconds,
            @Value("${jwt.refreshtoken-validity-in-seconds}") long refreshTokenValidityInSeconds) {
        this.secret = secret;
        this.accessTokenValidityInMilliseconds = accessTokenValidityInSeconds * 1000;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInSeconds * 1000;
    }
    
    // 빈이 생성되고 주입을 받은 후에 secret값을 Base64 Decode해서 key 변수에 할당하기 위해
    @Override
    public void afterPropertiesSet() {
    	char x; // sign bit 가진다 또는 안 가진다.
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    
    public String createToken(Authentication authentication, boolean isAccessToken) {
    	//권한들을 받아온다음에 map으로 권한을 문자열을 받아옵니다 "," 를 기준으로 나눕니다
    	
    	// 해당 코드는 인증된 사용자의 권한 목록을 문자열로 변환하는 역할을 합니다.
    	//각 권한(GrantedAuthority)에서 권한 이름을 가져온 후, 콤마(,)로 구분하여 하나의 문자열로 결합합니다.
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // 토큰의 expire 시간을 설정
        // Returns the number of milliseconds
        // since January 1, 1970, 00:00:00 GMTrepresented by this Date object.
        long now = (new Date()).getTime();        								   
        
        long date = 0;
        if (isAccessToken)
        	date = now + this.accessTokenValidityInMilliseconds;
        else
        	date = now + this.refreshTokenValidityInMilliseconds;
        
        Date validity = new Date(date);
        
        //jwt 토큰을 생성하는 코드
        return Jwts.builder()//jwt 객체를 초기화
                .setSubject(authentication.getName())//토큰의 주체(Subject)를 정함
                .claim(AUTHORITIES_KEY, authorities) // 정보 저장( 클레임은 JWT의 payload 부분에 저장되는 정보로, 이 코드에서는 AUTHORITIES_KEY라는 키 값에 authorities라는 권한 정보를 저장합니다.)
                .signWith(key, SignatureAlgorithm.HS512) // 사용할 암호화 알고리즘과 , signature 에 들어갈 secret값 세팅
                .setExpiration(validity) // set Expire Time 해당 옵션 안넣으면 expire안함
                .compact();
    }
    
    // 토큰 생성및 db에저장해둠 만듬
    public String createAndPersistRefreshTokenForUser(Authentication authentication) {
    	String refreshToken = this.createToken(authentication, false);
        
        long now = (new Date()).getTime();
        //리프래시 토큰의 유효기간?을 성정함
        Date validity = new Date(now + this.refreshTokenValidityInMilliseconds);
        Instant instant = validity.toInstant();
        
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault())
        		.toLocalDateTime();
        
    	String username = authentication.getName();
        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setUsername(username);
        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setExpiryDate(localDateTime); 
        refreshTokenRepository.save(refreshTokenEntity);

        return refreshToken;
    }
    
    // 토큰으로 클레임을 만들고 이를 이용해 유저 객체를 만들어서 최종적으로 authentication 객체를 리턴
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        //User(Long, String, String, String, boolean, Set<Authority>)
        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    // 토큰의 유효성 검증을 수행
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            
            log.info("잘못된 JWT 서명입니다.");
            System.out.println("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            
            log.info("만료된 JWT 토큰입니다.");
            System.out.println("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            
            log.info("지원되지 않는 JWT 토큰입니다.");
            System.out.println("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            
        }
        return false;
    }
    
    
    public Claims  getTokenInfo(TokenDto token)
    {
    	//parser는 토큰을 파싱해서 컴퓨터가 이해할 수 있는 형태로 변환하는 역할
    	// jwts.parser는 토큰을 컴퓨터가 이해할 수 있는 형태로 만든다음에 검증을 한다
    	return Jwts.parserBuilder()
    			.setSigningKey(key)
    			.build()
    			.parseClaimsJws(token.getToken())
    			.getBody();
    }
   
}

