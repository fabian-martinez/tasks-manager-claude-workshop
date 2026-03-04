package com.workshop.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Task Manager API - Main Application Class
 *
 * Demo application for Claude Code Workshop
 * This application demonstrates the complete Claude Code workflow
 * for implementing new features and managing existing code.
 *
 * Features implemented:
 * - User management
 * - Task management with status tracking
 * - Project organization
 * - Comment system
 * - Basic REST APIs
 *
 * Features pending (for workshop):
 * - Advanced task filtering (Story 1)
 * - Email notifications (Story 2)
 * - Reporting system (Story 3)
 * - JWT Authentication (Story 4)
 * - Audit logging (Story 5)
 *
 * @author Claude Code Workshop
 * @version 1.0.0
 */
@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
public class TaskManagerApplication {

    public static void main(String[] args) {
        System.out.println("🚀 Starting Task Manager API...");
        System.out.println("📋 Claude Code Workshop Demo Application");
        System.out.println("🌐 Server will be available at: http://localhost:8080");
        System.out.println("🗄️ H2 Database Console: http://localhost:8080/h2-console");
        System.out.println("📊 Health Check: http://localhost:8080/actuator/health");
        System.out.println("───────────────────────────────────────────────────");

        SpringApplication.run(TaskManagerApplication.class, args);

        System.out.println("✅ Task Manager API started successfully!");
        System.out.println("📚 API Endpoints:");
        System.out.println("   GET    /api/users           - List all users");
        System.out.println("   POST   /api/users           - Create user");
        System.out.println("   GET    /api/tasks           - List all tasks");
        System.out.println("   POST   /api/tasks           - Create task");
        System.out.println("   GET    /api/projects        - List all projects");
        System.out.println("   POST   /api/projects        - Create project");
        System.out.println("───────────────────────────────────────────────────");
    }
}