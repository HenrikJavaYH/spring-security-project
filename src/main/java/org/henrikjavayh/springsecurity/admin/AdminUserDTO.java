package org.henrikjavayh.springsecurity.admin;


import java.util.Set;

public record AdminUserDTO(
        String id,
        String username,
        Set<String> roles
) {}

