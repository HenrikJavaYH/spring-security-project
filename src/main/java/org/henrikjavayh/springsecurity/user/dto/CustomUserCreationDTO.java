package org.henrikjavayh.springsecurity.user.dto;



import jakarta.validation.constraints.*;

public record CustomUserCreationDTO(

        @Size(min = 2, max = 25, message = "2-25 letters")
        @NotBlank(message = "Username may not only contain whitespaces")
        String username,

        @Pattern(
                regexp = "^" +
                        "(?=.*[a-z])" +
                        "(?=.*[A-Z])" +
                        "(?=.*[0-9])" +
                        "(?=.*[ @$!%*?&])" +
                        ".+$",
                message = "Password must contain uppercase, lowercase, digit, and special character"
        )
        @Size(max = 80, message = "Maximum length of password exceeded")
        String password
) {}
