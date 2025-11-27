package org.henrikjavayh.springsecurity.user.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.henrikjavayh.springsecurity.user.CustomUser;
import org.henrikjavayh.springsecurity.user.autthority.UserRole;

import java.util.Set;
import java.util.UUID;

public record CustomUserCreationDTO(


        @Size(min = 2, max = 25, message = "2-25 letters")
        @NotBlank(message = "Username may not only contain whitespaces")
        String username,

        @Pattern(
                regexp = "^" +
                        "(?=.*[a-z])" + // at least one lowercase letter
                        "(?=.*[A-Z])" + // at least one uppercase letter
                        "(?=.*[0-9])" + // at least one digit
                        "(?=.*[ @$!%*?&])" + // at least one special character
                        ".+$", // one or more characters, until

                message = "Password must contain at least one uppercase, one lowercase, one digit, and one special character"

        )

        @Size(max = 80, message = "Maximum length of password exceeded")
        String password,
        @NotNull boolean isAccountNonExpired,
        @NotNull boolean isAccountNonLocked,
        @NotNull boolean isCredentialsNonExpired,
        @NotNull boolean isEnabled,
        //@notnull @AssertTrue = not null or false

        @NotEmpty
        @Pattern(
                regexp = "^GUEST|USER|ADMIN$",
                message = "Must choose a role"
        )
        Set<UserRole> roles


) {


}
