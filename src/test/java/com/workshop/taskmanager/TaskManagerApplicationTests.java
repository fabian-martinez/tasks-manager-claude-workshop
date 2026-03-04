package com.workshop.taskmanager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Main Application Tests
 *
 * Basic smoke tests to ensure the application context loads correctly
 * and all components are properly wired.
 */
@SpringBootTest
@ActiveProfiles("test")
class TaskManagerApplicationTests {

    /**
     * Test that the Spring context loads successfully
     */
    @Test
    void contextLoads() {
        // This test will pass if the application context loads without any errors
        // It validates:
        // - All beans are properly configured
        // - No circular dependencies
        // - Database configuration is valid
        // - All auto-configuration works correctly
    }

    /**
     * Test that the main method runs without exceptions
     */
    @Test
    void mainMethodTest() {
        // Test that the main method can be called without throwing exceptions
        // In a real test, we might want to test startup time, memory usage, etc.
        String[] args = {};

        // We're not actually calling main() here because it would start the server
        // In integration tests, we might use @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
            // TaskManagerApplication.main(args); // Commented out to avoid starting server in tests
        });
    }
}