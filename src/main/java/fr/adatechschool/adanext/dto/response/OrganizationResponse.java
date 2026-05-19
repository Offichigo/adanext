package fr.adatechschool.adanext.dto.response;

import fr.adatechschool.adanext.model.Organization;

import java.time.LocalDateTime;

public class OrganizationResponse {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private UserResponse owner;

    public static OrganizationResponse from(Organization organization) {
        OrganizationResponse response = new OrganizationResponse();
        response.id = organization.getId();
        response.name = organization.getName();
        response.description = organization.getDescription();
        response.createdAt = organization.getCreatedAt();
        response.owner = UserResponse.from(organization.getOwner());
        return response;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public UserResponse getOwner() { return owner; }
}
