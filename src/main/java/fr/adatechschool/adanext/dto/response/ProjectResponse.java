package fr.adatechschool.adanext.dto.response;

import fr.adatechschool.adanext.model.Project;

import java.time.LocalDateTime;

public class ProjectResponse {

    private Long id;
    private String name;
    private String description;
    private Project.Status status;
    private LocalDateTime createdAt;
    private Long organizationId;

    public static ProjectResponse from(Project project) {
        ProjectResponse response = new ProjectResponse();
        response.id = project.getId();
        response.name = project.getName();
        response.description = project.getDescription();
        response.status = project.getStatus();
        response.createdAt = project.getCreatedAt();
        response.organizationId = project.getOrganization().getId();
        return response;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Project.Status getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Long getOrganizationId() { return organizationId; }
}
