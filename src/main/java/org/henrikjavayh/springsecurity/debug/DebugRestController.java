package org.henrikjavayh.springsecurity.debug;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.henrikjavayh.springsecurity.user.CustomUser;
import org.henrikjavayh.springsecurity.user.CustomUserRepository;
import org.henrikjavayh.springsecurity.user.autthority.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/debug" )
public class DebugRestController {

    private final PasswordEncoder passwordEncoder;
    private final CustomUserRepository customUserRepository;

    @Autowired
    public DebugRestController(PasswordEncoder passwordEncoder, CustomUserRepository customUserRepository) {
        this.passwordEncoder = passwordEncoder;
        this.customUserRepository = customUserRepository;
    }

    @GetMapping("/who-am-i" )
    public String whoAmI() {
// Using SecurityContextHolder (global access point)
        SecurityContext context = SecurityContextHolder.getContext ();
        Authentication authentication = context. getAuthentication ();
// Using SecurityContext directly (once we have it)
        String username = authentication. getName();
        String authorities = authentication. getAuthorities ().toString ();
        return "Hello, " + username + "! Your roles: " + authorities;
    }

    /*

    @GetMapping("/who-am-i" )
    public String whoAmI(Authentication authentication ) {
        return "Hello, " + authentication .getName() +
                "! Your roles: " + authentication .getAuthorities ();
    }

     */

    @GetMapping("/auth-session")
    public ResponseEntity<String> debugAuthenticationSes(Authentication authentication) {
        System.out.println(authentication.getClass().getSimpleName());
        System.out.println(authentication.isAuthenticated());
        System.out.println(authentication);

        return ResponseEntity.ok().body("Check logs");
    }

    @GetMapping("/session-attributes" )
    public ResponseEntity <String> debugSessionAttributes (HttpServletRequest request) {
        HttpSession session = request.getSession (false);
        if (session == null) {
            return ResponseEntity .ok("No session found." );
        }
        StringBuilder sb = new StringBuilder ();
        sb.append("Session ID: " ).append(session.getId()).append("\n");
        sb.append("Attributes: \n");
        var names = session.getAttributeNames ();
        while (names.hasMoreElements ()) {
            String name = names.nextElement ();
            Object value = session.getAttribute (name);
            sb.append(" • ").append(name)
                    .append(" = ").append(value)
                    .append("\n");
        }
        return ResponseEntity .ok(sb.toString ());
    }

    @GetMapping("/create-debug-admin" )
    public ResponseEntity<String> createDebugAdmin() {

        try {
            customUserRepository.save(
                    new CustomUser(
                            "Frida",
                            passwordEncoder.encode("321" ),
                            true,
                            true,
                            true,
                            true,
                            Set.of(UserRole.ADMIN)

                    )

            );
            return ResponseEntity.status(HttpStatus.CREATED).body("User was successfully created!" );
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists " + e.getLocalizedMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Something went wrong.. " + e.getLocalizedMessage());
        } finally {
            System.out.println("Creating debug app ended" );
        }


    }

    @GetMapping
    public ResponseEntity<String> testBcryptEncoding(
            @RequestParam(value = "message" ) String message
    ) {

        String obfuscatedMessage = passwordEncoder.encode(message);

        return ResponseEntity.ok().body("Message was: " + message + " and was hashed: " + obfuscatedMessage);


    }
}
