package com.taskmanagement.dto;

import com.taskmanagement.entity.Task;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {

    @NotBlank(message = "Task title is required")
    @Size(max = 200, message = "Task title must be less than 200 characters")
    private String title;

    private String description;

    @NotNull(message = "Task status is required")
    private Task.Status status;

    @NotNull(message = "Task priority is required")
    private Task.Priority priority;

    private LocalDate dueDate;
    private LocalDateTime reminderDate;
    private Long categoryId;
    private List<String> labels;
}