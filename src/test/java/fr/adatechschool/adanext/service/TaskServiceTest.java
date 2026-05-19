package fr.adatechschool.adanext.service;

import fr.adatechschool.adanext.dto.request.CreateTaskRequest;
import fr.adatechschool.adanext.dto.response.TaskResponse;
import fr.adatechschool.adanext.exception.ResourceNotFoundException;
import fr.adatechschool.adanext.model.Organization;
import fr.adatechschool.adanext.model.Project;
import fr.adatechschool.adanext.model.Task;
import fr.adatechschool.adanext.model.User;
import fr.adatechschool.adanext.repository.OrganizationMemberRepository;
import fr.adatechschool.adanext.repository.ProjectRepository;
import fr.adatechschool.adanext.repository.TaskRepository;
import fr.adatechschool.adanext.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private OrganizationMemberRepository memberRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private User user;
    private Organization organization;
    private Project project;

    @BeforeEach
    void setUp() {
        user = new User("alice@example.com", "hashed", "Alice");
        organization = new Organization("Equipe Ada", null, user);
        project = new Project("Site vitrine", null, organization);
    }

    @Test
    void create_shouldReturnTaskWithTodoStatus() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userRepository.findByEmail("alice@example.com")).thenReturn(Optional.of(user));
        when(memberRepository.existsByOrganizationIdAndUserId(any(), any())).thenReturn(true);
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Creer la page d'accueil");

        TaskResponse response = taskService.create(1L, request, "alice@example.com");

        assertThat(response.getTitle()).isEqualTo("Creer la page d'accueil");
        assertThat(response.getStatus()).isEqualTo(Task.Status.TODO);
    }

    @Test
    void updateStatus_shouldChangeTaskStatus() {
        Task task = new Task("Ma tache", null, project);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findByEmail("alice@example.com")).thenReturn(Optional.of(user));
        when(memberRepository.existsByOrganizationIdAndUserId(any(), any())).thenReturn(true);

        TaskResponse response = taskService.updateStatus(1L, Task.Status.IN_PROGRESS, "alice@example.com");

        assertThat(response.getStatus()).isEqualTo(Task.Status.IN_PROGRESS);
    }

    @Test
    void create_shouldThrowWhenProjectNotFound() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Tache orpheline");

        assertThatThrownBy(() -> taskService.create(99L, request, "alice@example.com"))
            .isInstanceOf(ResourceNotFoundException.class);
    }
}
