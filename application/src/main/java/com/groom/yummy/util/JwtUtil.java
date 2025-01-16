package com.groom.yummy.util;

import com.groom.yummy.user.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key secretKey;
    private final long validTime;
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";


    public JwtUtil(@Value("${spring.jwt.secret}") String secretKey,
                   @Value("${spring.jwt.valid-time}")long validTime){
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.validTime = validTime;
    }

    public String createAccessToken(User user){
        Date timeNow = new Date(System.currentTimeMillis());
        Date expirationTime = new Date(timeNow.getTime() + validTime);

        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .claim("role",user.getRole())
                .claim("email",user.getEmail())
                .setIssuedAt(timeNow)
                .setExpiration(expirationTime)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsername(String token){
        String info = Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
        return info;
    }

    public boolean validateToken(String token){
        //log.info("토큰 유효성 검증 시작");
        return valid(secretKey, token);
    }

    private boolean valid(Key key, String token){
        try{
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        }catch (SignatureException ex){
            throw new RuntimeException();
        }catch (MalformedJwtException ex){
            throw new RuntimeException();
        }catch (ExpiredJwtException ex){
            throw new RuntimeException();
        }catch (IllegalArgumentException ex){
            throw new RuntimeException();
        }
    }

}


