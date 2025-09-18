package com.taskmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "task_labels", 
    indexes = {
        @Index(name = "idx_task_label_task_id", columnList = "task_id"),
        @Index(name = "idx_task_label_label", columnList = "label")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_task_label", columnNames = {"task_id", "label"})
    }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class TaskLabel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false, foreignKey = @ForeignKey(name = "fk_task_label_task"))
    private Task task;

    @NotBlank(message = "Label is required")
    @Size(max = 50, message = "Label must be less than 50 characters")
    @Column(nullable = false, length = 50)
    private String label;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void updateTimestamp() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}