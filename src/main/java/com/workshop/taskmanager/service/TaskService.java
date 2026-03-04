package com.workshop.taskmanager.service;

import com.workshop.taskmanager.model.Task;
import com.workshop.taskmanager.model.TaskStatus;
import com.workshop.taskmanager.model.User;
import com.workshop.taskmanager.model.Project;
import com.workshop.taskmanager.repository.TaskRepository;
import com.workshop.taskmanager.repository.UserRepository;
import com.workshop.taskmanager.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    /**
     * Get all tasks
     */
    @Transactional(readOnly = true)
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    /**
     * Get tasks with pagination
     */
    @Transactional(readOnly = true)
    public Page<Task> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    /**
     * Get task by ID
     */
    @Transactional(readOnly = true)
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    /**
     * Create new task
     */
    public Task createTask(Task task) {
        // Validate creator exists
        if (task.getCreatedBy() != null && task.getCreatedBy().getId() != null) {
            User creator = userRepository.findById(task.getCreatedBy().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Creator user not found with id: " + task.getCreatedBy().getId()));
            task.setCreatedBy(creator);
        } else {
            throw new IllegalArgumentException("Task must have a creator");
        }

        // Validate assignee exists (if provided)
        if (task.getAssignedTo() != null && task.getAssignedTo().getId() != null) {
            User assignee = userRepository.findById(task.getAssignedTo().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Assigned user not found with id: " + task.getAssignedTo().getId()));
            task.setAssignedTo(assignee);
        }

        // Validate project exists (if provided)
        if (task.getProject() != null && task.getProject().getId() != null) {
            Project project = projectRepository.findById(task.getProject().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + task.getProject().getId()));
            task.setProject(project);
        }

        return taskRepository.save(task);
    }

    /**
     * Update existing task
     */
    public Task updateTask(Long id, Task taskDetails) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + id));

        // Update basic fields
        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setStatus(taskDetails.getStatus());
        task.setDueDate(taskDetails.getDueDate());

        // Update assignee if provided
        if (taskDetails.getAssignedTo() != null && taskDetails.getAssignedTo().getId() != null) {
            User assignee = userRepository.findById(taskDetails.getAssignedTo().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Assigned user not found with id: " + taskDetails.getAssignedTo().getId()));
            task.setAssignedTo(assignee);
        } else {
            task.setAssignedTo(null);
        }

        // Update project if provided
        if (taskDetails.getProject() != null && taskDetails.getProject().getId() != null) {
            Project project = projectRepository.findById(taskDetails.getProject().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + taskDetails.getProject().getId()));
            task.setProject(project);
        } else {
            task.setProject(null);
        }

        return taskRepository.save(task);
    }

    /**
     * Delete task
     */
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new IllegalArgumentException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    /**
     * Get tasks by status
     */
    @Transactional(readOnly = true)
    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    /**
     * Get tasks assigned to a user
     */
    @Transactional(readOnly = true)
    public List<Task> getTasksAssignedToUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        return taskRepository.findByAssignedTo(user);
    }

    /**
     * Get tasks created by a user
     */
    @Transactional(readOnly = true)
    public List<Task> getTasksCreatedByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        return taskRepository.findByCreatedBy(user);
    }

    /**
     * Get tasks by project
     */
    @Transactional(readOnly = true)
    public List<Task> getTasksByProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + projectId));
        return taskRepository.findByProject(project);
    }

    /**
     * Search tasks by title
     */
    @Transactional(readOnly = true)
    public List<Task> searchTasksByTitle(String title) {
        return taskRepository.findByTitleContainingIgnoreCase(title);
    }

    /**
     * Get overdue tasks
     */
    @Transactional(readOnly = true)
    public List<Task> getOverdueTasks() {
        return taskRepository.findOverdueTasks(LocalDateTime.now());
    }

    /**
     * Get recent tasks (last 7 days)
     */
    @Transactional(readOnly = true)
    public List<Task> getRecentTasks() {
        return taskRepository.findRecentTasks(LocalDateTime.now().minusDays(7));
    }

    /**
     * Basic search with filters - to be enhanced in Story 1
     * This method provides a foundation for advanced filtering
     */
    @Transactional(readOnly = true)
    public Page<Task> searchTasks(TaskStatus status, Long assignedToId,
                                 LocalDateTime createdAfter, LocalDateTime createdBefore,
                                 Pageable pageable) {

        User assignedTo = null;
        if (assignedToId != null) {
            assignedTo = userRepository.findById(assignedToId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + assignedToId));
        }

        return taskRepository.findTasksWithFilters(status, assignedTo, createdAfter, createdBefore, pageable);
    }

    /**
     * Get task statistics for a user
     */
    @Transactional(readOnly = true)
    public List<Object[]> getTaskStatisticsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        return taskRepository.countTasksByStatusForUser(user);
    }

    /**
     * Assign task to user
     */
    public Task assignTask(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        task.setAssignedTo(user);
        return taskRepository.save(task);
    }

    /**
     * Update task status
     */
    public Task updateTaskStatus(Long taskId, TaskStatus status) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));

        task.setStatus(status);
        return taskRepository.save(task);
    }

    /**
     * Get total task count
     */
    @Transactional(readOnly = true)
    public long getTotalTaskCount() {
        return taskRepository.countTotalTasks();
    }

    /**
     * Check if task exists
     */
    @Transactional(readOnly = true)
    public boolean taskExists(Long id) {
        return taskRepository.existsById(id);
    }
}