package com.jwt.demo.service;



import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class BlackListService 
{
	private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BlackListService(JdbcTemplate jdbcTemplate) 
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 블랙리스트에 토큰 추가
    public void addToBlacklist(String tokenId, long expirationInMillis) {
        Timestamp expiration = new Timestamp(System.currentTimeMillis() + expirationInMillis);
        jdbcTemplate.update("INSERT INTO jwt_blacklist (token_id, expiration) VALUES (?, ?)", tokenId, expiration);
    }

    // 블랙리스트 확인
    public boolean isBlacklisted(String tokenId) {
        String sql = "SELECT COUNT(*) FROM jwt_blacklist WHERE token_id = ? AND expiration > CURRENT_TIMESTAMP";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tokenId);
        return count != null && count > 0;
    }

    // 만료된 블랙리스트 항목 삭제 (배치 작업으로 실행)
    public void cleanExpiredTokens() {
        String sql = "DELETE FROM jwt_blacklist WHERE expiration <= CURRENT_TIMESTAMP";
        jdbcTemplate.update(sql);
    }

}
