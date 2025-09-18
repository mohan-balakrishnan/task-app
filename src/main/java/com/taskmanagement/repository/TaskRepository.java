package com.taskmanagement.repository;

import com.taskmanagement.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByUserId(Long userId, Pageable pageable);

    Page<Task> findByUserIdAndStatus(Long userId, Task.Status status, Pageable pageable);

    Page<Task> findByUserIdAndPriority(Long userId, Task.Priority priority, Pageable pageable);

    Page<Task> findByUserIdAndCategoryId(Long userId, Long categoryId, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND " +
           "(:status IS NULL OR t.status = :status) AND " +
           "(:priority IS NULL OR t.priority = :priority) AND " +
           "(:categoryId IS NULL OR t.category.id = :categoryId) AND " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Task> findTasksWithFilters(
            @Param("userId") Long userId,
            @Param("status") Task.Status status,
            @Param("priority") Task.Priority priority,
            @Param("categoryId") Long categoryId,
            @Param("search") String search,
            Pageable pageable);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.user.id = :userId AND t.status = :status")
    long countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Task.Status status);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.user.id = :userId AND t.dueDate < :currentDate AND t.status != 'COMPLETED'")
    long countOverdueTasksByUserId(@Param("userId") Long userId, @Param("currentDate") LocalDate currentDate);

    List<Task> findTop5ByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT t FROM Task t WHERE t.reminderDate <= :now AND t.status != 'COMPLETED'")
    List<Task> findTasksWithReminders(@Param("now") java.time.LocalDateTime now);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.category.id = :categoryId")
    long countByCategoryId(@Param("categoryId") Long categoryId);

    List<Task> findByDueDateBetweenAndStatusNot(LocalDate startDate, LocalDate endDate, Task.Status status);
}