package fr.adatechschool.adanext.controller;

import fr.adatechschool.adanext.dto.request.CreateTaskRequest;
import fr.adatechschool.adanext.dto.request.UpdateTaskStatusRequest;
import fr.adatechschool.adanext.dto.response.TaskResponse;
import fr.adatechschool.adanext.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/tasks")
@Tag(name = "Taches", description = "Gestion des taches")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @Operation(summary = "Ajouter une tache a un projet")
    public ResponseEntity<TaskResponse> create(
        @PathVariable Long projectId,
        @Valid @RequestBody CreateTaskRequest request,
        @AuthenticationPrincipal UserDetails currentUser
    ) {
        TaskResponse response = taskService.create(projectId, request, currentUser.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Lister les taches d'un projet")
    public ResponseEntity<List<TaskResponse>> findAll(
        @PathVariable Long projectId,
        @AuthenticationPrincipal UserDetails currentUser
    ) {
        return ResponseEntity.ok(taskService.findAllByProject(projectId, currentUser.getUsername()));
    }

    @PatchMapping("/{taskId}/status")
    @Operation(summary = "Changer le statut d'une tache (kanban)")
    public ResponseEntity<TaskResponse> updateStatus(
        @PathVariable Long projectId,
        @PathVariable Long taskId,
        @Valid @RequestBody UpdateTaskStatusRequest request,
        @AuthenticationPrincipal UserDetails currentUser
    ) {
        TaskResponse response = taskService.updateStatus(taskId, request.getStatus(), currentUser.getUsername());
        return ResponseEntity.ok(response);
    }
}
