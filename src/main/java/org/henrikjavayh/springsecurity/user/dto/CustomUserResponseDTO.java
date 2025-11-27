package org.henrikjavayh.springsecurity.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Use this class to hide sensitive info
 *
 **/

public record CustomUserResponseDTO(

        @Size(min = 2, max = 25, message = "2-25 letters")
        @NotBlank(message = "Username may not only contain whitespaces")
        String username
) {
}
