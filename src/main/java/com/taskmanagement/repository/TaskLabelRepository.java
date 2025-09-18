package com.taskmanagement.repository;

import com.taskmanagement.entity.TaskLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskLabelRepository extends JpaRepository<TaskLabel, Long> {

    List<TaskLabel> findByTaskId(Long taskId);

    @Modifying
    @Query("DELETE FROM TaskLabel tl WHERE tl.task.id = :taskId")
    void deleteByTaskId(@Param("taskId") Long taskId);

    @Modifying
    @Query("DELETE FROM TaskLabel tl WHERE tl.task.id = :taskId AND tl.label = :label")
    void deleteByTaskIdAndLabel(@Param("taskId") Long taskId, @Param("label") String label);

    @Query("SELECT DISTINCT tl.label FROM TaskLabel tl WHERE tl.task.user.id = :userId ORDER BY tl.label")
    List<String> findDistinctLabelsByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(tl) FROM TaskLabel tl WHERE tl.task.id = :taskId")
    long countByTaskId(@Param("taskId") Long taskId);
}