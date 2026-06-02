package com.example.classroom.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
  private final SecretKey key;
  private final long expireMillis;

  public JwtService(@Value("${jwt.secret}") String secret,
                    @Value("${jwt.expire-minutes}") long expireMinutes) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.expireMillis = expireMinutes * 60_000L;
  }

  public String createToken(Long userId, String username, String role) {
    Date now = new Date();
    return Jwts.builder()
        .subject(String.valueOf(userId))
        .claim("username", username)
        .claim("role", role)
        .issuedAt(now)
        .expiration(new Date(now.getTime() + expireMillis))
        .signWith(key)
        .compact();
  }

  public CurrentUser parse(String token) {
    Claims claims = Jwts.parser().verifyWith(key).build()
        .parseSignedClaims(token).getPayload();
    return new CurrentUser(Long.valueOf(claims.getSubject()),
        claims.get("username", String.class),
        claims.get("role", String.class));
  }
}
