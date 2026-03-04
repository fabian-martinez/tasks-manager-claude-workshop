package com.workshop.taskmanager.repository;

import com.workshop.taskmanager.model.Task;
import com.workshop.taskmanager.model.TaskStatus;
import com.workshop.taskmanager.model.User;
import com.workshop.taskmanager.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Find tasks by status
     */
    List<Task> findByStatus(TaskStatus status);

    /**
     * Find tasks assigned to a specific user
     */
    List<Task> findByAssignedTo(User assignedTo);

    /**
     * Find tasks created by a specific user
     */
    List<Task> findByCreatedBy(User createdBy);

    /**
     * Find tasks by project
     */
    List<Task> findByProject(Project project);

    /**
     * Find tasks by title containing the given text (case-insensitive)
     */
    @Query("SELECT t FROM Task t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Task> findByTitleContainingIgnoreCase(@Param("title") String title);

    /**
     * Find overdue tasks (due date passed and not completed)
     */
    @Query("SELECT t FROM Task t WHERE t.dueDate < :currentDate AND t.status != 'DONE'")
    List<Task> findOverdueTasks(@Param("currentDate") LocalDateTime currentDate);

    /**
     * Count tasks by status for a specific user
     */
    @Query("SELECT t.status, COUNT(t) FROM Task t WHERE t.assignedTo = :user GROUP BY t.status")
    List<Object[]> countTasksByStatusForUser(@Param("user") User user);

    /**
     * Find tasks with pagination
     */
    Page<Task> findAll(Pageable pageable);

    /**
     * Advanced search method for filters (Story 1 will expand this)
     * This is a basic implementation that can be enhanced with Criteria API
     */
    @Query("SELECT t FROM Task t WHERE " +
           "(:status is null OR t.status = :status) AND " +
           "(:assignedTo is null OR t.assignedTo = :assignedTo) AND " +
           "(:createdAfter is null OR t.createdAt >= :createdAfter) AND " +
           "(:createdBefore is null OR t.createdAt <= :createdBefore)")
    Page<Task> findTasksWithFilters(
            @Param("status") TaskStatus status,
            @Param("assignedTo") User assignedTo,
            @Param("createdAfter") LocalDateTime createdAfter,
            @Param("createdBefore") LocalDateTime createdBefore,
            Pageable pageable
    );

    /**
     * Find recent tasks (created in the last N days)
     */
    @Query("SELECT t FROM Task t WHERE t.createdAt >= :sinceDate ORDER BY t.createdAt DESC")
    List<Task> findRecentTasks(@Param("sinceDate") LocalDateTime sinceDate);

    /**
     * Count total tasks
     */
    @Query("SELECT COUNT(t) FROM Task t")
    long countTotalTasks();

    /**
     * Find tasks by project with status filter
     */
    @Query("SELECT t FROM Task t WHERE t.project = :project AND (:status is null OR t.status = :status)")
    List<Task> findByProjectAndStatus(@Param("project") Project project, @Param("status") TaskStatus status);
}