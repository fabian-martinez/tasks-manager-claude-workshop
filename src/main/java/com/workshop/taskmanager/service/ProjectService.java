package com.workshop.taskmanager.service;

import com.workshop.taskmanager.model.Project;
import com.workshop.taskmanager.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    /**
     * Get all projects
     */
    @Transactional(readOnly = true)
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    /**
     * Get project by ID
     */
    @Transactional(readOnly = true)
    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    /**
     * Create new project
     */
    public Project createProject(Project project) {
        if (projectRepository.existsByName(project.getName())) {
            throw new IllegalArgumentException("Project with name '" + project.getName() + "' already exists");
        }
        return projectRepository.save(project);
    }

    /**
     * Update existing project
     */
    public Project updateProject(Long id, Project projectDetails) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + id));

        // Check if name is being changed and if new name already exists
        if (!project.getName().equals(projectDetails.getName()) &&
            projectRepository.existsByName(projectDetails.getName())) {
            throw new IllegalArgumentException("Project with name '" + projectDetails.getName() + "' already exists");
        }

        project.setName(projectDetails.getName());
        project.setDescription(projectDetails.getDescription());

        return projectRepository.save(project);
    }

    /**
     * Delete project
     */
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new IllegalArgumentException("Project not found with id: " + id);
        }
        projectRepository.deleteById(id);
    }

    /**
     * Search projects by name
     */
    @Transactional(readOnly = true)
    public List<Project> searchProjectsByName(String name) {
        return projectRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Get task count for a project
     */
    @Transactional(readOnly = true)
    public long getTaskCount(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + projectId));
        return projectRepository.countTasksByProject(project);
    }

    /**
     * Check if project exists
     */
    @Transactional(readOnly = true)
    public boolean projectExists(Long id) {
        return projectRepository.existsById(id);
    }

    /**
     * Check if project name is already taken
     */
    @Transactional(readOnly = true)
    public boolean projectNameExists(String name) {
        return projectRepository.existsByName(name);
    }

    /**
     * Get projects with their task counts
     */
    @Transactional(readOnly = true)
    public List<Object[]> getProjectsWithTaskCounts() {
        return projectRepository.findProjectsWithTaskCount();
    }
}