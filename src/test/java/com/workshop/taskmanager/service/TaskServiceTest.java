package com.workshop.taskmanager.service;

import com.workshop.taskmanager.model.Task;
import com.workshop.taskmanager.model.TaskStatus;
import com.workshop.taskmanager.model.User;
import com.workshop.taskmanager.repository.TaskRepository;
import com.workshop.taskmanager.repository.UserRepository;
import com.workshop.taskmanager.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TaskService
 *
 * These tests validate the business logic in TaskService
 * using mocked repositories to isolate the service layer.
 *
 * Note: This is a basic test suite that can be expanded during
 * the workshop when implementing advanced filtering (Story 1).
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private TaskService taskService;

    private User testUser;
    private Task testTask;

    @BeforeEach
    void setUp() {
        testUser = new User("John Doe", "john.doe@example.com");
        testUser.setId(1L);

        testTask = new Task("Test Task", "Test Description", testUser);
        testTask.setId(1L);
        testTask.setStatus(TaskStatus.TODO);
    }

    @Test
    void getAllTasks_ShouldReturnAllTasks() {
        // Arrange
        List<Task> expectedTasks = Arrays.asList(testTask);
        when(taskRepository.findAll()).thenReturn(expectedTasks);

        // Act
        List<Task> actualTasks = taskService.getAllTasks();

        // Assert
        assertEquals(expectedTasks.size(), actualTasks.size());
        assertEquals(expectedTasks, actualTasks);
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void getTaskById_WhenTaskExists_ShouldReturnTask() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));

        // Act
        Optional<Task> result = taskService.getTaskById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testTask, result.get());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void getTaskById_WhenTaskDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Task> result = taskService.getTaskById(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(taskRepository, times(1)).findById(999L);
    }

    @Test
    void createTask_WhenCreatorExists_ShouldCreateTask() {
        // Arrange
        Task newTask = new Task("New Task", "New Description", testUser);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(taskRepository.save(any(Task.class))).thenReturn(newTask);

        // Act
        Task result = taskService.createTask(newTask);

        // Assert
        assertEquals(newTask, result);
        verify(userRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(newTask);
    }

    @Test
    void createTask_WhenCreatorDoesNotExist_ShouldThrowException() {
        // Arrange
        Task newTask = new Task("New Task", "New Description", testUser);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> taskService.createTask(newTask)
        );

        assertEquals("Creator user not found with id: 1", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void createTask_WhenNoCreator_ShouldThrowException() {
        // Arrange
        Task newTask = new Task("New Task", "New Description", null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> taskService.createTask(newTask)
        );

        assertEquals("Task must have a creator", exception.getMessage());
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void getTasksByStatus_ShouldReturnTasksWithStatus() {
        // Arrange
        List<Task> expectedTasks = Arrays.asList(testTask);
        when(taskRepository.findByStatus(TaskStatus.TODO)).thenReturn(expectedTasks);

        // Act
        List<Task> result = taskService.getTasksByStatus(TaskStatus.TODO);

        // Assert
        assertEquals(expectedTasks, result);
        verify(taskRepository, times(1)).findByStatus(TaskStatus.TODO);
    }

    @Test
    void getTasksAssignedToUser_WhenUserExists_ShouldReturnTasks() {
        // Arrange
        List<Task> expectedTasks = Arrays.asList(testTask);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(taskRepository.findByAssignedTo(testUser)).thenReturn(expectedTasks);

        // Act
        List<Task> result = taskService.getTasksAssignedToUser(1L);

        // Assert
        assertEquals(expectedTasks, result);
        verify(userRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).findByAssignedTo(testUser);
    }

    @Test
    void getTasksAssignedToUser_WhenUserDoesNotExist_ShouldThrowException() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> taskService.getTasksAssignedToUser(999L)
        );

        assertEquals("User not found with id: 999", exception.getMessage());
        verify(userRepository, times(1)).findById(999L);
        verify(taskRepository, never()).findByAssignedTo(any(User.class));
    }

    @Test
    void updateTaskStatus_WhenTaskExists_ShouldUpdateStatus() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        // Act
        Task result = taskService.updateTaskStatus(1L, TaskStatus.IN_PROGRESS);

        // Assert
        assertEquals(TaskStatus.IN_PROGRESS, result.getStatus());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(testTask);
    }

    @Test
    void updateTaskStatus_WhenTaskDoesNotExist_ShouldThrowException() {
        // Arrange
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> taskService.updateTaskStatus(999L, TaskStatus.DONE)
        );

        assertEquals("Task not found with id: 999", exception.getMessage());
        verify(taskRepository, times(1)).findById(999L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void assignTask_WhenTaskAndUserExist_ShouldAssignTask() {
        // Arrange
        User assignee = new User("Jane Smith", "jane@example.com");
        assignee.setId(2L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(userRepository.findById(2L)).thenReturn(Optional.of(assignee));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        // Act
        Task result = taskService.assignTask(1L, 2L);

        // Assert
        assertEquals(assignee, result.getAssignedTo());
        verify(taskRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(2L);
        verify(taskRepository, times(1)).save(testTask);
    }

    @Test
    void deleteTask_WhenTaskExists_ShouldDeleteTask() {
        // Arrange
        when(taskRepository.existsById(1L)).thenReturn(true);

        // Act
        taskService.deleteTask(1L);

        // Assert
        verify(taskRepository, times(1)).existsById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTask_WhenTaskDoesNotExist_ShouldThrowException() {
        // Arrange
        when(taskRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> taskService.deleteTask(999L)
        );

        assertEquals("Task not found with id: 999", exception.getMessage());
        verify(taskRepository, times(1)).existsById(999L);
        verify(taskRepository, never()).deleteById(any(Long.class));
    }
}