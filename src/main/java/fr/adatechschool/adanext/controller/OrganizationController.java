package fr.adatechschool.adanext.controller;

import fr.adatechschool.adanext.dto.request.CreateOrganizationRequest;
import fr.adatechschool.adanext.dto.response.OrganizationResponse;
import fr.adatechschool.adanext.service.OrganizationService;
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
@RequestMapping("/api/organizations")
@Tag(name = "Organisations", description = "Gestion des organisations")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping
    @Operation(summary = "Creer une organisation")
    public ResponseEntity<OrganizationResponse> create(
        @Valid @RequestBody CreateOrganizationRequest request,
        @AuthenticationPrincipal UserDetails currentUser
    ) {
        OrganizationResponse response = organizationService.create(request, currentUser.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Lister mes organisations")
    public ResponseEntity<List<OrganizationResponse>> findAll(
        @AuthenticationPrincipal UserDetails currentUser
    ) {
        return ResponseEntity.ok(organizationService.findAllForUser(currentUser.getUsername()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Voir une organisation")
    public ResponseEntity<OrganizationResponse> findById(
        @PathVariable Long id,
        @AuthenticationPrincipal UserDetails currentUser
    ) {
        return ResponseEntity.ok(organizationService.findById(id, currentUser.getUsername()));
    }
}
