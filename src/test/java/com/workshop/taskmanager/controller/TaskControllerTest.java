package com.workshop.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workshop.taskmanager.model.Task;
import com.workshop.taskmanager.model.TaskStatus;
import com.workshop.taskmanager.model.User;
import com.workshop.taskmanager.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Basic unit tests for TaskController
 *
 * These tests validate the HTTP endpoints for task management.
 * This is a basic test suite that can be expanded during the workshop
 * when implementing advanced filtering (Story 1).
 */
@WebMvcTest(TaskController.class)
@ActiveProfiles("test")
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

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
    @WithMockUser
    void getAllTasks_ShouldReturnTasksPage() throws Exception {
        // Arrange
        Page<Task> tasksPage = new PageImpl<>(Arrays.asList(testTask), PageRequest.of(0, 20), 1);
        when(taskService.getAllTasks(any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(tasksPage);

        // Act & Assert
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));

        verify(taskService, times(1)).getAllTasks(any(org.springframework.data.domain.Pageable.class));
    }

    @Test
    @WithMockUser
    void getTaskById_WhenTaskExists_ShouldReturnTask() throws Exception {
        // Arrange
        when(taskService.getTaskById(1L)).thenReturn(Optional.of(testTask));

        // Act & Assert
        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"));

        verify(taskService, times(1)).getTaskById(1L);
    }

    @Test
    @WithMockUser
    void getTaskById_WhenTaskDoesNotExist_ShouldReturn404() throws Exception {
        // Arrange
        when(taskService.getTaskById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/tasks/999"))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).getTaskById(999L);
    }

    @Test
    @WithMockUser
    void getTotalTaskCount_ShouldReturnCount() throws Exception {
        // Arrange
        when(taskService.getTotalTaskCount()).thenReturn(10L);

        // Act & Assert
        mockMvc.perform(get("/api/tasks/count"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalTasks").value(10));

        verify(taskService, times(1)).getTotalTaskCount();
    }
}