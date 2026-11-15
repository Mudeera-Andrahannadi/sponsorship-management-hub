package com.sponsorhub.security;

import com.sponsorhub.entity.Role;
import com.sponsorhub.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService
{
    private final SecretKey key;
    private final long life;

    public JwtService(@Value("${hub.jwt-secret}") String secret, @Value("${hub.jwt-hours}") int hours)
    {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.life = Duration.ofHours(hours).toMillis();
    }

    public String issue(User user)
    {
        Date now = new Date();
        return Jwts.builder()
            .subject(String.valueOf(user.getUserId()))
            .claim("role", user.getRole().name())
            .issuedAt(now)
            .expiration(new Date(now.getTime() + life))
            .signWith(key)
            .compact();
    }

    public Me read(String token)
    {
        try
        {
            Claims body = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
            return new Me(Integer.valueOf(body.getSubject()), Role.valueOf(body.get("role", String.class)));
        }
        catch (JwtException | IllegalArgumentException ex)
        {
            return null;
        }
    }
}
