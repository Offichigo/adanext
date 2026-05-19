package fr.adatechschool.adanext.service;

import fr.adatechschool.adanext.dto.request.CreateProjectRequest;
import fr.adatechschool.adanext.dto.response.ProjectResponse;
import fr.adatechschool.adanext.exception.AccessDeniedException;
import fr.adatechschool.adanext.exception.ResourceNotFoundException;
import fr.adatechschool.adanext.model.Organization;
import fr.adatechschool.adanext.model.Project;
import fr.adatechschool.adanext.model.User;
import fr.adatechschool.adanext.repository.OrganizationMemberRepository;
import fr.adatechschool.adanext.repository.OrganizationRepository;
import fr.adatechschool.adanext.repository.ProjectRepository;
import fr.adatechschool.adanext.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository memberRepository;
    private final UserRepository userRepository;

    public ProjectService(
        ProjectRepository projectRepository,
        OrganizationRepository organizationRepository,
        OrganizationMemberRepository memberRepository,
        UserRepository userRepository
    ) {
        this.projectRepository = projectRepository;
        this.organizationRepository = organizationRepository;
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ProjectResponse create(Long organizationId, CreateProjectRequest request, String userEmail) {
        Organization organization = findOrganizationById(organizationId);
        checkMembership(organizationId, userEmail);

        Project project = new Project(request.getName(), request.getDescription(), organization);
        projectRepository.save(project);

        return ProjectResponse.from(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> findAllByOrganization(Long organizationId, String userEmail) {
        findOrganizationById(organizationId);
        checkMembership(organizationId, userEmail);

        return projectRepository
            .findAllByOrganizationId(organizationId)
            .stream()
            .map(ProjectResponse::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public ProjectResponse findById(Long id, String userEmail) {
        Project project = findProjectById(id);
        checkMembership(project.getOrganization().getId(), userEmail);
        return ProjectResponse.from(project);
    }

    private Organization findOrganizationById(Long id) {
        return organizationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Organisation introuvable : " + id));
    }

    private Project findProjectById(Long id) {
        return projectRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Projet introuvable : " + id));
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
