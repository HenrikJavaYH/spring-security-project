package org.henrikjavayh.springsecurity.admin;


import org.henrikjavayh.springsecurity.user.CustomUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final CustomUserRepository userRepo;


    public AdminService(CustomUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public List<AdminUserDTO> getAllUsers() {
        return userRepo.findAll()
                .stream()
                .map(u -> new AdminUserDTO(
                        u.getId().toString(),
                        u.getUsername(),
                        u.getRoles().stream()
                                .map(Enum::name)
                                .collect(Collectors.toSet())
                ))
                .collect(Collectors.toList());
    }

    public void deleteUser(UUID id) {
        if (!userRepo.existsById(id)) {
            throw new IllegalArgumentException("User not found");
        }
        userRepo.deleteById(id);
    }
}

