package org.henrikjavayh.springsecurity.view;


import jakarta.validation.Valid;
import org.henrikjavayh.springsecurity.user.CustomUser;
import org.henrikjavayh.springsecurity.user.CustomUserRepository;
import org.henrikjavayh.springsecurity.user.autthority.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Set;

@Controller
public class CustomViewController {

    private final CustomUserRepository customUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomViewController(CustomUserRepository customUserRepository, PasswordEncoder passwordEncoder) {
        this.customUserRepository = customUserRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @GetMapping("/admin")
    public String adminPage () {

        return "adminpage";
    }

    @GetMapping("/user")
    public String userPage () {

        return "userpage";
    }

    @GetMapping("/register")
    public String registerPage (Model model) {

        model.addAttribute("customUser", new CustomUser());

        return "registerpage";
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid CustomUser customUser, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "registerpage";
        }

        customUser.setPassword(passwordEncoder.encode(customUser.getPassword()));

        customUser.setAccountNonExpired(true);
        customUser.setAccountNonLocked(true);
        customUser.setCredentialsNonExpired(true);
        customUser.setEnabled(true);

        customUser.setRoles(
                Set.of(UserRole.USER)
        );

        System.out.println("Saving user..");
        customUserRepository.save(customUser);

        return "redirect:/login";
    }
}
