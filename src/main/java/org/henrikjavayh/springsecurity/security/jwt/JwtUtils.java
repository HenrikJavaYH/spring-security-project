package org.henrikjavayh.springsecurity.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.henrikjavayh.springsecurity.user.CustomUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private final String base64EncodedSecretKey = "finland";
    private final byte[] keyBytes = Base64.getDecoder().decode(base64EncodedSecretKey);
    private final SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);

    private final int jwtExpirationMs = (int) TimeUnit.HOURS.toMillis(1);

    public String generateJwtToken(CustomUser customUser) {
        logger.debug("generate Jwt for user: {} with roles: {}", customUser.getUsername(), customUser.getRoles());

        List<String> roles = customUser.getRoles().stream().map(
                userRole -> userRole.getRoleName()
        ).toList();

        String token = Jwts.builder()
                .subject(customUser.getUsername())
                .claim("authorities", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(secretKey)
                .compact();

        logger.info("Generated Jwt for user: {}", customUser.getUsername());
        return token;
    }

    public String getUsernameFromJwtToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            String username = claims.getSubject();
            logger.debug("Extracted username: '{}' from JWT token", username);
            return username;
        } catch (Exception e) {
            logger.warn("Failed to extract username from JWT token {}",e.getMessage());
            return null;
        }
    }

    String extractJwtFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if ("authToken".equals(cookie.getName())) {     // Cookie should be named authToken
                return cookie.getValue();
            }
        }
        return null;
    }

    //Used to pass in jwt token for validation
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(authToken);

            logger.debug("Jwt validation successfull!");
            return true;
        } catch (Exception e) {
            logger.error("Jwt validation failed: {} " + e.getMessage());
        }
        return false;
    }
}
