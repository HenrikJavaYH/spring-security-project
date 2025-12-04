package org.henrikjavayh.springsecurity.todo;

import org.henrikjavayh.springsecurity.todo.dto.TodoRequest;
import org.henrikjavayh.springsecurity.todo.dto.TodoResponse;
import org.henrikjavayh.springsecurity.user.CustomUser;
import org.henrikjavayh.springsecurity.user.CustomUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final CustomUserRepository userRepository;

    public TodoService(TodoRepository todoRepository, CustomUserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    public List<TodoResponse> getAllByUsername(String username) {
        CustomUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return todoRepository.findByOwner(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TodoResponse createForUsername(TodoRequest request, String username) {
        CustomUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Todo todo = new Todo();
        todo.setTitle(request.getTitle().trim());
        todo.setDescription(request.getDescription() != null ? request.getDescription().trim() : null);
        todo.setCompleted(request.isCompleted());
        todo.setOwner(user);
        todo.setUpdatedAt(Instant.now());

        todoRepository.save(todo);

        return toResponse(todo);
    }

    public TodoResponse updateForUsername(UUID id, TodoRequest request, String username) {
        CustomUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found"));

        if (!todo.getOwner().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your todo");
        }

        todo.setTitle(request.getTitle().trim());
        todo.setDescription(request.getDescription());
        todo.setCompleted(request.isCompleted());
        todo.setUpdatedAt(Instant.now());

        todoRepository.save(todo);

        return toResponse(todo);
    }

    public void deleteForUsername(UUID id, String username) {
        CustomUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found"));

        if (!todo.getOwner().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your todo");
        }

        todoRepository.delete(todo);
    }

    private TodoResponse toResponse(Todo t) {
        return new TodoResponse(
                t.getId(),
                t.getTitle(),
                t.getDescription(),
                t.isCompleted(),
                t.getCreatedAt(),
                t.getUpdatedAt()
        );
    }
}
