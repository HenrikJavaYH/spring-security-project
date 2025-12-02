package org.henrikjavayh.springsecurity.user.dto;

public record CustomUserLoginDTO(
        String username,
        String password
) {
}
