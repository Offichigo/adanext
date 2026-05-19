package fr.adatechschool.adanext.controller;

import fr.adatechschool.adanext.dto.request.CreateProjectRequest;
import fr.adatechschool.adanext.dto.response.ProjectResponse;
import fr.adatechschool.adanext.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/organizations/{organizationId}/projects")
@Tag(name = "Projets", description = "Gestion des projets")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    @Operation(summary = "Creer un projet dans une organisation")
    public ResponseEntity<ProjectResponse> create(
        @PathVariable Long organizationId,
        @Valid @RequestBody CreateProjectRequest request,
        @AuthenticationPrincipal UserDetails currentUser
    ) {
        ProjectResponse response = projectService.create(organizationId, request, currentUser.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Lister les projets d'une organisation")
    public ResponseEntity<List<ProjectResponse>> findAll(
        @PathVariable Long organizationId,
        @AuthenticationPrincipal UserDetails currentUser
    ) {
        return ResponseEntity.ok(projectService.findAllByOrganization(organizationId, currentUser.getUsername()));
    }

    @GetMapping("/{projectId}")
    @Operation(summary = "Voir un projet")
    public ResponseEntity<ProjectResponse> findById(
        @PathVariable Long organizationId,
        @PathVariable Long projectId,
        @AuthenticationPrincipal UserDetails currentUser
    ) {
        return ResponseEntity.ok(projectService.findById(projectId, currentUser.getUsername()));
    }
}
