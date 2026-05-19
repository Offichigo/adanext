package fr.adatechschool.adanext.dto.response;

import fr.adatechschool.adanext.model.Task;

import java.time.LocalDateTime;

public class TaskResponse {

    private Long id;
    private String title;
    private String description;
    private Task.Status status;
    private LocalDateTime createdAt;
    private Long projectId;

    public static TaskResponse from(Task task) {
        TaskResponse response = new TaskResponse();
        response.id = task.getId();
        response.title = task.getTitle();
        response.description = task.getDescription();
        response.status = task.getStatus();
        response.createdAt = task.getCreatedAt();
        response.projectId = task.getProject().getId();
        return response;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Task.Status getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Long getProjectId() { return projectId; }
}
