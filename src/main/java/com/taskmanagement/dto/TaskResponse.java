package com.taskmanagement.dto;

import com.taskmanagement.entity.Task;
import com.taskmanagement.entity.TaskLabel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private Task.Status status;
    private Task.Priority priority;
    private LocalDate dueDate;
    private LocalDateTime reminderDate;
    private Long userId;
    private String username;
    private Long categoryId;
    private String categoryName;
    private String categoryColor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
    private List<String> labels;

    public static TaskResponse fromEntity(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .dueDate(task.getDueDate())
                .reminderDate(task.getReminderDate())
                .userId(task.getUser().getId())
                .username(task.getUser().getUsername())
                .categoryId(task.getCategory() != null ? task.getCategory().getId() : null)
                .categoryName(task.getCategory() != null ? task.getCategory().getName() : null)
                .categoryColor(task.getCategory() != null ? task.getCategory().getColor() : null)
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .completedAt(task.getCompletedAt())
                .labels(task.getLabels() != null ? 
                    task.getLabels().stream()
                        .map(TaskLabel::getLabel)
                        .collect(Collectors.toList()) : null)
                .build();
    }
}