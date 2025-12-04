package org.henrikjavayh.springsecurity.todo;

import org.henrikjavayh.springsecurity.todo.dto.TodoRequest;
import org.henrikjavayh.springsecurity.todo.dto.TodoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TodoResponse>> getAllMyTodos(@AuthenticationPrincipal UserDetails me) {
        return ResponseEntity.ok(service.getAllByUsername(me.getUsername()));
    }

    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(@AuthenticationPrincipal UserDetails me,
                                                   @Valid @RequestBody TodoRequest request) {
        return ResponseEntity.ok(service.createForUsername(request, me.getUsername()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoResponse> updateTodo(@AuthenticationPrincipal UserDetails me,
                                                   @PathVariable UUID id,
                                                   @Valid @RequestBody TodoRequest request) {
        return ResponseEntity.ok(service.updateForUsername(id, request, me.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@AuthenticationPrincipal UserDetails me,
                                           @PathVariable UUID id) {
        service.deleteForUsername(id, me.getUsername());
        return ResponseEntity.noContent().build();
    }
}
