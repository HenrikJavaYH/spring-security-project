package org.henrikjavayh.springsecurity.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


    @RestController
    @RequestMapping("/api/admin")
    public class AdminController {

        private final AdminService service;

        public AdminController(AdminService service) {
            this.service = service;
        }

        @GetMapping("/users")
        public ResponseEntity<List<AdminUserDTO>> getAllUsers() {
            return ResponseEntity.ok(service.getAllUsers());
        }

        @DeleteMapping("/users/{id}")
        public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
            service.deleteUser(id);
            return ResponseEntity.noContent().build();
        }
    }


