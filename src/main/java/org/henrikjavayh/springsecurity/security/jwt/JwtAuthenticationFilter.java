package org.henrikjavayh.springsecurity.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.henrikjavayh.springsecurity.user.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.http.HttpHeaders;

import java.io.IOException;



@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtUtils jwtUtils, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtils = jwtUtils;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal (
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse  response,
            @NonNull FilterChain filterChain
            ) throws ServletException, IOException {

        logger.debug("-----JWTauthenticationFilter STARTED-----");

        String token = extractJwtFromCookie(request);
        if (token == null) {
            token = extractJwtFromRequest(request);
        }

        if  (token == null) {
            logger.debug("No jwt Token found in request");
            filterChain.doFilter(request, response);
            return;
        }
        logger.debug("JWT Token found: {}",token);

        if (jwtUtils.validateJwtToken(token)) {
            String username = jwtUtils.getUsernameFromJwtToken(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                if (userDetails != null && userDetails.isEnabled()) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    logger.debug("Authenticaded (DB verified) user: {} ", username);
                } else  {
                    logger.debug("User not found or disabled {}", username);
                }

                filterChain.doFilter(request, response);
                logger.debug("------JWTauthenticationFilter ENDED-----");
            }
        }
    }


    private String extractJwtFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
