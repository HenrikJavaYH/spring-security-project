package org.henrikjavayh.springsecurity.logout;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LogoutController {

    @PostMapping("/api/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // Ta bort JWT-cookie
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // tar bort cookien
        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of("message", "Du är utloggad"));
    }
}

