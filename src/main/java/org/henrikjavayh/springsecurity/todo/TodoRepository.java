package org.henrikjavayh.springsecurity.todo;

import org.henrikjavayh.springsecurity.user.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TodoRepository extends JpaRepository<Todo, UUID> {

        List<Todo> findByOwner(CustomUser owner);



}
