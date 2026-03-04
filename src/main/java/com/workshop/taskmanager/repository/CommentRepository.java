package com.workshop.taskmanager.repository;

import com.workshop.taskmanager.model.Comment;
import com.workshop.taskmanager.model.Task;
import com.workshop.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Find comments by task
     */
    List<Comment> findByTaskOrderByCreatedAtDesc(Task task);

    /**
     * Find comments by author
     */
    List<Comment> findByAuthor(User author);

    /**
     * Find recent comments for a task (last N days)
     */
    @Query("SELECT c FROM Comment c WHERE c.task = :task AND c.createdAt >= :sinceDate ORDER BY c.createdAt DESC")
    List<Comment> findRecentCommentsByTask(@Param("task") Task task, @Param("sinceDate") LocalDateTime sinceDate);

    /**
     * Count comments by task
     */
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.task = :task")
    long countByTask(@Param("task") Task task);

    /**
     * Find comments containing specific text (case-insensitive)
     */
    @Query("SELECT c FROM Comment c WHERE LOWER(c.content) LIKE LOWER(CONCAT('%', :content, '%'))")
    List<Comment> findByContentContainingIgnoreCase(@Param("content") String content);

    /**
     * Find latest comment for each task
     */
    @Query("SELECT c FROM Comment c WHERE c.createdAt = (SELECT MAX(c2.createdAt) FROM Comment c2 WHERE c2.task = c.task)")
    List<Comment> findLatestCommentsPerTask();
}