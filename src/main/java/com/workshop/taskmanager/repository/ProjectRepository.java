package com.workshop.taskmanager.repository;

import com.workshop.taskmanager.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    /**
     * Find projects by name containing the given text (case-insensitive)
     */
    @Query("SELECT p FROM Project p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Project> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Count tasks by project
     */
    @Query("SELECT COUNT(t) FROM Task t WHERE t.project = :project")
    long countTasksByProject(@Param("project") Project project);

    /**
     * Find projects with their task count
     */
    @Query("SELECT p, COUNT(t) FROM Project p LEFT JOIN p.tasks t GROUP BY p")
    List<Object[]> findProjectsWithTaskCount();

    /**
     * Check if project name exists
     */
    boolean existsByName(String name);
}