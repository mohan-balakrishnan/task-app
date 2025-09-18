package com.taskmanagement.repository;

import com.taskmanagement.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUserId(Long userId);

    Page<Category> findByUserId(Long userId, Pageable pageable);

    Optional<Category> findByIdAndUserId(Long id, Long userId);

    boolean existsByNameAndUserId(String name, Long userId);

    @Query("SELECT c FROM Category c WHERE c.user.id = :userId AND " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Category> findByUserIdAndSearchTerm(
            @Param("userId") Long userId, 
            @Param("search") String search, 
            Pageable pageable);

    @Query("SELECT COUNT(c) FROM Category c WHERE c.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);
}