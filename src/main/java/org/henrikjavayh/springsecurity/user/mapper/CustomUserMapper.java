package org.henrikjavayh.springsecurity.user.mapper;

import org.henrikjavayh.springsecurity.user.CustomUser;
import org.henrikjavayh.springsecurity.user.dto.CustomUserCreationDTO;
import org.henrikjavayh.springsecurity.user.dto.CustomUserResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CustomUserMapper {

    public CustomUser toEntity(CustomUserCreationDTO customUserCreationDTO) {

        return new CustomUser(
                customUserCreationDTO.username(),
                customUserCreationDTO.password(),
                customUserCreationDTO.isAccountNonExpired(),
                customUserCreationDTO.isAccountNonLocked(),
                customUserCreationDTO.isCredentialsNonExpired(),
                customUserCreationDTO.isEnabled(),
                customUserCreationDTO.roles()
        );
    }

    public CustomUserResponseDTO toUsernameDTO(CustomUser customUser) {

        return new CustomUserResponseDTO(customUser.getUsername());
    }
}
