package com.workshop.taskmanager.controller;

import com.workshop.taskmanager.model.Comment;
import com.workshop.taskmanager.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Get all comments
     * GET /api/comments
     */
    @GetMapping
    public ResponseEntity<List<Comment>> getAllComments() {
        try {
            List<Comment> comments = commentService.getAllComments();
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get comment by ID
     * GET /api/comments/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        try {
            Optional<Comment> comment = commentService.getCommentById(id);
            if (comment.isPresent()) {
                return ResponseEntity.ok(comment.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Create new comment
     * POST /api/comments
     */
    @PostMapping
    public ResponseEntity<?> createComment(@Valid @RequestBody Comment comment) {
        try {
            Comment createdComment = commentService.createComment(comment);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating comment: " + e.getMessage());
        }
    }

    /**
     * Update existing comment
     * PUT /api/comments/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Long id, @Valid @RequestBody Comment commentDetails) {
        try {
            Comment updatedComment = commentService.updateComment(id, commentDetails);
            return ResponseEntity.ok(updatedComment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating comment: " + e.getMessage());
        }
    }

    /**
     * Delete comment
     * DELETE /api/comments/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        try {
            commentService.deleteComment(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting comment: " + e.getMessage());
        }
    }

    /**
     * Get comments by task
     * GET /api/comments/task/{taskId}
     */
    @GetMapping("/task/{taskId}")
    public ResponseEntity<?> getCommentsByTask(@PathVariable Long taskId) {
        try {
            List<Comment> comments = commentService.getCommentsByTask(taskId);
            return ResponseEntity.ok(comments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving comments for task: " + e.getMessage());
        }
    }

    /**
     * Get comments by author
     * GET /api/comments/author/{authorId}
     */
    @GetMapping("/author/{authorId}")
    public ResponseEntity<?> getCommentsByAuthor(@PathVariable Long authorId) {
        try {
            List<Comment> comments = commentService.getCommentsByAuthor(authorId);
            return ResponseEntity.ok(comments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving comments by author: " + e.getMessage());
        }
    }

    /**
     * Get recent comments for task
     * GET /api/comments/task/{taskId}/recent
     */
    @GetMapping("/task/{taskId}/recent")
    public ResponseEntity<?> getRecentCommentsByTask(@PathVariable Long taskId) {
        try {
            List<Comment> comments = commentService.getRecentCommentsByTask(taskId);
            return ResponseEntity.ok(comments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving recent comments: " + e.getMessage());
        }
    }

    /**
     * Get comment count for task
     * GET /api/comments/task/{taskId}/count
     */
    @GetMapping("/task/{taskId}/count")
    public ResponseEntity<?> getCommentCountByTask(@PathVariable Long taskId) {
        try {
            long count = commentService.getCommentCountByTask(taskId);
            return ResponseEntity.ok(java.util.Map.of("commentCount", count));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error getting comment count: " + e.getMessage());
        }
    }

    /**
     * Search comments by content
     * GET /api/comments/search?content={content}
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchComments(@RequestParam String content) {
        try {
            List<Comment> comments = commentService.searchCommentsByContent(content);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error searching comments: " + e.getMessage());
        }
    }

    /**
     * Get latest comments per task
     * GET /api/comments/latest-per-task
     */
    @GetMapping("/latest-per-task")
    public ResponseEntity<?> getLatestCommentsPerTask() {
        try {
            List<Comment> comments = commentService.getLatestCommentsPerTask();
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error getting latest comments: " + e.getMessage());
        }
    }
}