package com.workshop.taskmanager.controller;

import com.workshop.taskmanager.service.CommentService;
import com.workshop.taskmanager.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({TaskController.class, CommentController.class})
@ActiveProfiles("test")
public class CorsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private CommentService commentService;

    @Test
    public void testTaskControllerCorsWithAllowedOrigin() throws Exception {
        mockMvc.perform(get("/api/tasks")
                .header("Origin", "http://localhost:3000"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"));
    }

    @Test
    public void testCommentControllerCorsWithAllowedOrigin() throws Exception {
        mockMvc.perform(get("/api/comments")
                .header("Origin", "http://localhost:3000"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"));
    }

    @Test
    public void testCorsWithForbiddenOrigin() throws Exception {
        mockMvc.perform(get("/api/tasks")
                .header("Origin", "http://forbidden-origin.com"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testPreflightWithAllowedOrigin() throws Exception {
        mockMvc.perform(options("/api/tasks")
                .header("Access-Control-Request-Method", "GET")
                .header("Origin", "http://localhost:3000"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"));
    }

    @Test
    public void testPreflightWithForbiddenOrigin() throws Exception {
        mockMvc.perform(options("/api/tasks")
                .header("Access-Control-Request-Method", "GET")
                .header("Origin", "http://forbidden-origin.com"))
                .andExpect(status().isForbidden());
    }
}
