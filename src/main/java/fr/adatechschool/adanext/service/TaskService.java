package fr.adatechschool.adanext.service;

import fr.adatechschool.adanext.dto.request.CreateTaskRequest;
import fr.adatechschool.adanext.dto.response.TaskResponse;
import fr.adatechschool.adanext.exception.AccessDeniedException;
import fr.adatechschool.adanext.exception.ResourceNotFoundException;
import fr.adatechschool.adanext.model.Project;
import fr.adatechschool.adanext.model.Task;
import fr.adatechschool.adanext.model.User;
import fr.adatechschool.adanext.repository.OrganizationMemberRepository;
import fr.adatechschool.adanext.repository.ProjectRepository;
import fr.adatechschool.adanext.repository.TaskRepository;
import fr.adatechschool.adanext.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final OrganizationMemberRepository memberRepository;
    private final UserRepository userRepository;

    public TaskService(
        TaskRepository taskRepository,
        ProjectRepository projectRepository,
        OrganizationMemberRepository memberRepository,
        UserRepository userRepository
    ) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public TaskResponse create(Long projectId, CreateTaskRequest request, String userEmail) {
        Project project = findProjectById(projectId);
        checkMembership(project.getOrganization().getId(), userEmail);

        Task task = new Task(request.getTitle(), request.getDescription(), project);
        taskRepository.save(task);

        return TaskResponse.from(task);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> findAllByProject(Long projectId, String userEmail) {
        Project project = findProjectById(projectId);
        checkMembership(project.getOrganization().getId(), userEmail);

        return taskRepository
            .findAllByProjectId(projectId)
            .stream()
            .map(TaskResponse::from)
            .toList();
    }

    @Transactional
    public TaskResponse updateStatus(Long taskId, Task.Status newStatus, String userEmail) {
        Task task = findTaskById(taskId);
        checkMembership(task.getProject().getOrganization().getId(), userEmail);

        task.setStatus(newStatus);
        return TaskResponse.from(task);
    }

    /*
     * TODO : ajouter une methode assignTask(Long taskId, Long userId, String callerEmail)
     * qui assigne une tache a un membre de l'organisation.
     * Verifier que l'utilisateur cible est bien membre de l'organisation.
     */

    private Project findProjectById(Long id) {
        return projectRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Projet introuvable : " + id));
    }

    private Task findTaskById(Long id) {
        return taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tache introuvable : " + id));
    }

    private void checkMembership(Long organizationId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable : " + userEmail));
        boolean isMember = memberRepository.existsByOrganizationIdAndUserId(organizationId, user.getId());
        if (!isMember) {
            throw new AccessDeniedException("Vous n'etes pas membre de cette organisation");
        }
    }
}
