package com.workshop.taskmanager.service;

import com.workshop.taskmanager.model.Comment;
import com.workshop.taskmanager.model.Task;
import com.workshop.taskmanager.model.User;
import com.workshop.taskmanager.repository.CommentRepository;
import com.workshop.taskmanager.repository.TaskRepository;
import com.workshop.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, TaskRepository taskRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    /**
     * Get all comments
     */
    @Transactional(readOnly = true)
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    /**
     * Get comment by ID
     */
    @Transactional(readOnly = true)
    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    /**
     * Create new comment
     */
    public Comment createComment(Comment comment) {
        // Validate task exists
        if (comment.getTask() != null && comment.getTask().getId() != null) {
            Task task = taskRepository.findById(comment.getTask().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + comment.getTask().getId()));
            comment.setTask(task);
        } else {
            throw new IllegalArgumentException("Comment must be associated with a task");
        }

        // Validate author exists
        if (comment.getAuthor() != null && comment.getAuthor().getId() != null) {
            User author = userRepository.findById(comment.getAuthor().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Author not found with id: " + comment.getAuthor().getId()));
            comment.setAuthor(author);
        } else {
            throw new IllegalArgumentException("Comment must have an author");
        }

        return commentRepository.save(comment);
    }

    /**
     * Update existing comment
     */
    public Comment updateComment(Long id, Comment commentDetails) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found with id: " + id));

        comment.setContent(commentDetails.getContent());
        return commentRepository.save(comment);
    }

    /**
     * Delete comment
     */
    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new IllegalArgumentException("Comment not found with id: " + id);
        }
        commentRepository.deleteById(id);
    }

    /**
     * Get comments for a specific task
     */
    @Transactional(readOnly = true)
    public List<Comment> getCommentsByTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));
        return commentRepository.findByTaskOrderByCreatedAtDesc(task);
    }

    /**
     * Get comments by author
     */
    @Transactional(readOnly = true)
    public List<Comment> getCommentsByAuthor(Long authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Author not found with id: " + authorId));
        return commentRepository.findByAuthor(author);
    }

    /**
     * Get recent comments for a task (last 7 days)
     */
    @Transactional(readOnly = true)
    public List<Comment> getRecentCommentsByTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));
        return commentRepository.findRecentCommentsByTask(task, LocalDateTime.now().minusDays(7));
    }

    /**
     * Get comment count for a task
     */
    @Transactional(readOnly = true)
    public long getCommentCountByTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));
        return commentRepository.countByTask(task);
    }

    /**
     * Search comments by content
     */
    @Transactional(readOnly = true)
    public List<Comment> searchCommentsByContent(String content) {
        return commentRepository.findByContentContainingIgnoreCase(content);
    }

    /**
     * Get latest comments per task
     */
    @Transactional(readOnly = true)
    public List<Comment> getLatestCommentsPerTask() {
        return commentRepository.findLatestCommentsPerTask();
    }

    /**
     * Check if comment exists
     */
    @Transactional(readOnly = true)
    public boolean commentExists(Long id) {
        return commentRepository.existsById(id);
    }
}