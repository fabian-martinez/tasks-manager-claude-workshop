package com.workshop.taskmanager.controller;

import com.workshop.taskmanager.model.Task;
import com.workshop.taskmanager.model.TaskStatus;
import com.workshop.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Get all tasks (with optional pagination)
     * GET /api/tasks
     * GET /api/tasks?page=0&size=10&sort=createdAt,desc
     */
    @GetMapping
    public ResponseEntity<?> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            Page<Task> tasks = taskService.getAllTasks(pageable);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving tasks: " + e.getMessage());
        }
    }

    /**
     * Get task by ID
     * GET /api/tasks/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        try {
            Optional<Task> task = taskService.getTaskById(id);
            if (task.isPresent()) {
                return ResponseEntity.ok(task.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Create new task
     * POST /api/tasks
     */
    @PostMapping
    public ResponseEntity<?> createTask(@Valid @RequestBody Task task) {
        try {
            Task createdTask = taskService.createTask(task);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating task: " + e.getMessage());
        }
    }

    /**
     * Update existing task
     * PUT /api/tasks/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @Valid @RequestBody Task taskDetails) {
        try {
            Task updatedTask = taskService.updateTask(id, taskDetails);
            return ResponseEntity.ok(updatedTask);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating task: " + e.getMessage());
        }
    }

    /**
     * Delete task
     * DELETE /api/tasks/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting task: " + e.getMessage());
        }
    }

    /**
     * Get tasks by status
     * GET /api/tasks/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getTasksByStatus(@PathVariable TaskStatus status) {
        try {
            List<Task> tasks = taskService.getTasksByStatus(status);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving tasks by status: " + e.getMessage());
        }
    }

    /**
     * Get tasks assigned to user
     * GET /api/tasks/assigned/{userId}
     */
    @GetMapping("/assigned/{userId}")
    public ResponseEntity<?> getTasksAssignedToUser(@PathVariable Long userId) {
        try {
            List<Task> tasks = taskService.getTasksAssignedToUser(userId);
            return ResponseEntity.ok(tasks);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving assigned tasks: " + e.getMessage());
        }
    }

    /**
     * Get tasks created by user
     * GET /api/tasks/created-by/{userId}
     */
    @GetMapping("/created-by/{userId}")
    public ResponseEntity<?> getTasksCreatedByUser(@PathVariable Long userId) {
        try {
            List<Task> tasks = taskService.getTasksCreatedByUser(userId);
            return ResponseEntity.ok(tasks);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving created tasks: " + e.getMessage());
        }
    }

    /**
     * Get tasks by project
     * GET /api/tasks/project/{projectId}
     */
    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> getTasksByProject(@PathVariable Long projectId) {
        try {
            List<Task> tasks = taskService.getTasksByProject(projectId);
            return ResponseEntity.ok(tasks);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving project tasks: " + e.getMessage());
        }
    }

    /**
     * Search tasks by title
     * GET /api/tasks/search?title={title}
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchTasks(@RequestParam String title) {
        try {
            List<Task> tasks = taskService.searchTasksByTitle(title);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error searching tasks: " + e.getMessage());
        }
    }

    /**
     * Advanced search with filters - Foundation for Story 1
     * GET /api/tasks/advanced-search?status=IN_PROGRESS&assignedTo=1&createdAfter=2024-01-01&createdBefore=2024-12-31&page=0&size=10
     */
    @GetMapping("/advanced-search")
    public ResponseEntity<?> advancedSearchTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) Long assignedTo,
            @RequestParam(required = false) String createdAfter,
            @RequestParam(required = false) String createdBefore,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            // Parse date parameters
            LocalDateTime createdAfterDate = null;
            LocalDateTime createdBeforeDate = null;

            if (createdAfter != null && !createdAfter.trim().isEmpty()) {
                createdAfterDate = LocalDateTime.parse(createdAfter + "T00:00:00");
            }

            if (createdBefore != null && !createdBefore.trim().isEmpty()) {
                createdBeforeDate = LocalDateTime.parse(createdBefore + "T23:59:59");
            }

            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            Page<Task> tasks = taskService.searchTasks(status, assignedTo, createdAfterDate, createdBeforeDate, pageable);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error in advanced search: " + e.getMessage());
        }
    }

    /**
     * Get overdue tasks
     * GET /api/tasks/overdue
     */
    @GetMapping("/overdue")
    public ResponseEntity<?> getOverdueTasks() {
        try {
            List<Task> tasks = taskService.getOverdueTasks();
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving overdue tasks: " + e.getMessage());
        }
    }

    /**
     * Get recent tasks
     * GET /api/tasks/recent
     */
    @GetMapping("/recent")
    public ResponseEntity<?> getRecentTasks() {
        try {
            List<Task> tasks = taskService.getRecentTasks();
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving recent tasks: " + e.getMessage());
        }
    }

    /**
     * Assign task to user
     * PUT /api/tasks/{taskId}/assign/{userId}
     */
    @PutMapping("/{taskId}/assign/{userId}")
    public ResponseEntity<?> assignTask(@PathVariable Long taskId, @PathVariable Long userId) {
        try {
            Task task = taskService.assignTask(taskId, userId);
            return ResponseEntity.ok(task);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error assigning task: " + e.getMessage());
        }
    }

    /**
     * Update task status
     * PUT /api/tasks/{taskId}/status
     */
    @PutMapping("/{taskId}/status")
    public ResponseEntity<?> updateTaskStatus(@PathVariable Long taskId, @RequestBody TaskStatus status) {
        try {
            Task task = taskService.updateTaskStatus(taskId, status);
            return ResponseEntity.ok(task);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating task status: " + e.getMessage());
        }
    }

    /**
     * Get task statistics for user
     * GET /api/tasks/stats/user/{userId}
     */
    @GetMapping("/stats/user/{userId}")
    public ResponseEntity<?> getTaskStatsByUser(@PathVariable Long userId) {
        try {
            List<Object[]> stats = taskService.getTaskStatisticsByUser(userId);
            return ResponseEntity.ok(stats);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error getting task statistics: " + e.getMessage());
        }
    }

    /**
     * Get total task count
     * GET /api/tasks/count
     */
    @GetMapping("/count")
    public ResponseEntity<?> getTotalTaskCount() {
        try {
            long count = taskService.getTotalTaskCount();
            return ResponseEntity.ok(java.util.Map.of("totalTasks", count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error getting task count: " + e.getMessage());
        }
    }
}