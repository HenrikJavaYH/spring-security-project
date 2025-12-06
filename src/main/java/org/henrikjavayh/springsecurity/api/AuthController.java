package org.henrikjavayh.springsecurity.api;

import jakarta.validation.Valid;
import org.henrikjavayh.springsecurity.user.CustomUser;
import org.henrikjavayh.springsecurity.user.CustomUserRepository;
import org.henrikjavayh.springsecurity.user.autthority.UserRole;
import org.henrikjavayh.springsecurity.user.dto.CustomUserCreationDTO;
import org.henrikjavayh.springsecurity.user.mapper.CustomUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final CustomUserRepository customUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserMapper customUserMapper;

    @Autowired
    public AuthController(CustomUserRepository customUserRepository,
                          PasswordEncoder passwordEncoder,
                          CustomUserMapper customUserMapper) {
        this.customUserRepository = customUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.customUserMapper = customUserMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody CustomUserCreationDTO dto) {

        try {

            // Kolla om användarnamnet redan finns
            if (customUserRepository.existsByUsername(dto.username())) {
                return ResponseEntity
                        .badRequest()
                        .body("Username already exists");
            }

            // Mappa DTO till entitet
            CustomUser customUser = customUserMapper.toEntity(dto);

            // Kryptera lösenord
            customUser.setPassword(customUser.getPassword(), passwordEncoder);


            // Sätt alla kontoflaggor
            customUser.setAccountNonExpired(true);
            customUser.setAccountNonLocked(true);
            customUser.setCredentialsNonExpired(true);
            customUser.setEnabled(true);

            // Ge alltid rollen USER
            customUser.setRoles(Set.of(UserRole.USER));

            // Spara användaren
            customUserRepository.save(customUser);

            return ResponseEntity.ok("User registered successfully");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
