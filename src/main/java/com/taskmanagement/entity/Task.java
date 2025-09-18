package com.taskmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tasks", indexes = {
    @Index(name = "idx_task_user_id", columnList = "user_id"),
    @Index(name = "idx_task_status", columnList = "status"),
    @Index(name = "idx_task_priority", columnList = "priority"),
    @Index(name = "idx_task_due_date", columnList = "due_date"),
    @Index(name = "idx_task_category_id", columnList = "category_id"),
    @Index(name = "idx_task_created_at", columnList = "created_at")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Task title is required")
    @Size(max = 200, message = "Task title must be less than 200 characters")
    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false, length = 20)
    private Status status = Status.PENDING;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false, length = 20)
    private Priority priority = Priority.MEDIUM;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "reminder_date")
    private LocalDateTime reminderDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_task_user"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "fk_task_category"))
    private Category category;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TaskLabel> labels;

    public enum Status {
        PENDING, IN_PROGRESS, COMPLETED
    }

    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    @PreUpdate
    @PrePersist
    public void updateTimestamp() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        updatedAt = LocalDateTime.now();

        // Set completed timestamp when status changes to COMPLETED
        if (status == Status.COMPLETED && completedAt == null) {
            completedAt = LocalDateTime.now();
        } else if (status != Status.COMPLETED) {
            completedAt = null;
        }
    }
}