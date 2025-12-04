package org.henrikjavayh.springsecurity.todo;

import jakarta.persistence.*;
import org.henrikjavayh.springsecurity.user.CustomUser;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "todos")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 1000)
    private String description;

    private boolean completed = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private CustomUser owner;

    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    public Todo() {}

    public UUID getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public CustomUser getOwner() { return owner; }
    public void setOwner(CustomUser owner) { this.owner = owner; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
