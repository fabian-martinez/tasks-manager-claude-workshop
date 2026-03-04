package com.workshop.taskmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

/**
 * Database Configuration
 *
 * Configures the H2 in-memory database for the Task Manager application.
 * This configuration is designed for workshop purposes and provides
 * a simple, self-contained database setup.
 *
 * In production, this would be replaced with a proper database
 * like PostgreSQL, MySQL, or SQL Server.
 */
@Configuration
public class DatabaseConfig {

    /**
     * Development database configuration
     * Uses H2 in-memory database with console enabled
     */
    @Bean
    @Profile("dev")
    public DataSource devDataSource() {
        EmbeddedDatabase database = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName("taskmanager-dev")
                .build();

        System.out.println("📊 Development database configured: H2 in-memory");
        System.out.println("🔗 JDBC URL: jdbc:h2:mem:taskmanager-dev");
        System.out.println("👤 Username: sa, Password: password");

        return database;
    }

    /**
     * Test database configuration
     * Separate database for tests to avoid conflicts
     */
    @Bean
    @Profile("test")
    public DataSource testDataSource() {
        EmbeddedDatabase database = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName("taskmanager-test")
                .build();

        System.out.println("🧪 Test database configured: H2 in-memory");

        return database;
    }

    /**
     * Production database configuration placeholder
     * In a real application, this would connect to a production database
     */
    @Bean
    @Profile("prod")
    public DataSource prodDataSource() {
        // In production, this would typically use:
        // - HikariCP connection pooling
        // - External database (PostgreSQL, MySQL, etc.)
        // - Proper connection parameters from environment variables

        System.out.println("🏭 Production profile detected - using default DataSource configuration");
        System.out.println("⚠️  Note: In real production, configure external database connection here");

        // For workshop purposes, still using H2
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName("taskmanager-prod")
                .build();
    }
}