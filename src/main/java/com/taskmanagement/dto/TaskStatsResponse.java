package com.taskmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatsResponse {
    private long totalTasks;
    private long pendingTasks;
    private long inProgressTasks;
    private long completedTasks;
    private long overdueTasks;
}