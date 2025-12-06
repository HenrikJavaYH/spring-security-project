package org.henrikjavayh.springsecurity.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomUserRepository extends JpaRepository<CustomUser, UUID> {

    Optional<CustomUser> findByUsername(String username);

    boolean existsByUsername(String username);



}
