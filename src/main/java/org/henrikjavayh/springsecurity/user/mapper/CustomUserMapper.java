package org.henrikjavayh.springsecurity.user.mapper;

import org.henrikjavayh.springsecurity.user.CustomUser;
import org.henrikjavayh.springsecurity.user.autthority.UserRole;
import org.henrikjavayh.springsecurity.user.dto.CustomUserCreationDTO;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CustomUserMapper {

    public CustomUser toEntity(CustomUserCreationDTO dto) {
        return new CustomUser(
                dto.username(),
                dto.password(),
                true,  // accountNonExpired
                true,  // accountNonLocked
                true,  // credentialsNonExpired
                true,  // enabled
                Set.of(UserRole.USER) // standardroll
        );
    }
}
