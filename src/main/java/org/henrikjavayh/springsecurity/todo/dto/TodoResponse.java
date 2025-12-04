package org.henrikjavayh.springsecurity.todo.dto;

import java.time.Instant;
import java.util.UUID;

public class TodoResponse {

    private UUID id;
    private String title;
    private String description;
    private boolean completed;
    private Instant createdAt;
    private Instant updatedAt;

    public TodoResponse(UUID id, String title, String description, boolean completed, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public boolean isCompleted() { return completed; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
