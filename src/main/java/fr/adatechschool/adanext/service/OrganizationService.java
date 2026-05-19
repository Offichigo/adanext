package fr.adatechschool.adanext.service;

import fr.adatechschool.adanext.dto.request.CreateOrganizationRequest;
import fr.adatechschool.adanext.dto.response.OrganizationResponse;
import fr.adatechschool.adanext.exception.AccessDeniedException;
import fr.adatechschool.adanext.exception.ResourceNotFoundException;
import fr.adatechschool.adanext.model.Organization;
import fr.adatechschool.adanext.model.OrganizationMember;
import fr.adatechschool.adanext.model.User;
import fr.adatechschool.adanext.repository.OrganizationMemberRepository;
import fr.adatechschool.adanext.repository.OrganizationRepository;
import fr.adatechschool.adanext.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository memberRepository;
    private final UserRepository userRepository;

    public OrganizationService(
        OrganizationRepository organizationRepository,
        OrganizationMemberRepository memberRepository,
        UserRepository userRepository
    ) {
        this.organizationRepository = organizationRepository;
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public OrganizationResponse create(CreateOrganizationRequest request, String ownerEmail) {
        User owner = findUserByEmail(ownerEmail);

        Organization organization = new Organization(
            request.getName(),
            request.getDescription(),
            owner
        );
        organizationRepository.save(organization);

        OrganizationMember membership = new OrganizationMember(
            organization,
            owner,
            OrganizationMember.Role.OWNER
        );
        memberRepository.save(membership);

        return OrganizationResponse.from(organization);
    }

    @Transactional(readOnly = true)
    public List<OrganizationResponse> findAllForUser(String userEmail) {
        User user = findUserByEmail(userEmail);
        return organizationRepository
            .findAllByMemberId(user.getId())
            .stream()
            .map(OrganizationResponse::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public OrganizationResponse findById(Long id, String userEmail) {
        Organization organization = findOrganizationById(id);
        checkMembership(organization.getId(), userEmail);
        return OrganizationResponse.from(organization);
    }

    private Organization findOrganizationById(Long id) {
        return organizationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Organisation introuvable : " + id));
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable : " + email));
    }

    private void checkMembership(Long organizationId, String userEmail) {
        User user = findUserByEmail(userEmail);
        boolean isMember = memberRepository.existsByOrganizationIdAndUserId(organizationId, user.getId());
        if (!isMember) {
            throw new AccessDeniedException("Vous n'etes pas membre de cette organisation");
        }
    }
}
