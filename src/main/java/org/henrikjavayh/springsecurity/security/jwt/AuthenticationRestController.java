package org.henrikjavayh.springsecurity.security.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.henrikjavayh.springsecurity.config.RabbitConfig;
import org.henrikjavayh.springsecurity.user.CustomUserDetails;
import org.henrikjavayh.springsecurity.user.dto.CustomUserLoginDTO;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthenticationRestController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final AmqpTemplate amqpTemplate;

    @Autowired
    public AuthenticationRestController(JwtUtils jwtUtils, AuthenticationManager authenticationManager, AmqpTemplate amqpTemplate) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.amqpTemplate = amqpTemplate;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(
            @RequestBody CustomUserLoginDTO customUserLoginDTO,
            HttpServletResponse response
    ) {
        logger.debug("Attempting authentication for user {} ", customUserLoginDTO.username());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        customUserLoginDTO.username(),
                        customUserLoginDTO.password())
        );

        System.out.println("\n---------AUTHENTICATION RESULT--------");
        System.out.println("Class: " + authentication.getClass());
        System.out.println("Authenticated User: " + authentication.isAuthenticated());


        Object principal = authentication.getPrincipal();
        System.out.println("Principal type: " + principal.getClass().getSimpleName());
        if (principal instanceof CustomUserDetails userDetails) {
            System.out.println("Username: " + userDetails.getUsername());
            System.out.println("Password(HASHED): " + userDetails.getPassword());
            System.out.println("Authorities: " + userDetails.getAuthorities());
            System.out.println("isAccount non-locked: " + userDetails.isAccountNonLocked());
            System.out.println("isAccountEnabled: " + userDetails.isEnabled());
        } else {
            System.out.println("Principal value: " + principal);
        }

        System.out.println("Credentials: " + authentication.getCredentials());
        System.out.println("Authorities: " + authentication.getAuthorities());
        System.out.println("Details: " + authentication.getDetails());
        System.out.println("====================================\n");

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String token = jwtUtils.generateJwtToken(customUserDetails.getCustomUser());

        Cookie cookie = new Cookie("token", token);//Måste namn matcha
        cookie.setPath("/");
        response.addCookie(cookie);
        cookie.setAttribute("SameSite", "Lax");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(3600);

        logger.info("Authentication successful for user {}", customUserLoginDTO.username());

        amqpTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_NAME,
                RabbitConfig.ROUTING_KEY,
                "User logged in, todo: Send email to alert them"
        );

        return ResponseEntity.ok(Map.of(
                "username", customUserLoginDTO.username(),
                "authorities", customUserDetails.getAuthorities(),
                "token", token
                ));

    }

}
