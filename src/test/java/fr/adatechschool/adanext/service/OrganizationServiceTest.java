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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceTest {

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private OrganizationMemberRepository memberRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrganizationService organizationService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("alice@example.com", "hashed", "Alice");
    }

    @Test
    void create_shouldSaveOrganizationAndOwnerMembership() {
        when(userRepository.findByEmail("alice@example.com")).thenReturn(Optional.of(user));
        when(organizationRepository.save(any(Organization.class))).thenAnswer(inv -> inv.getArgument(0));
        when(memberRepository.save(any(OrganizationMember.class))).thenAnswer(inv -> inv.getArgument(0));

        CreateOrganizationRequest request = new CreateOrganizationRequest();
        request.setName("Equipe Ada");
        request.setDescription("Notre organisation");

        OrganizationResponse response = organizationService.create(request, "alice@example.com");

        assertThat(response.getName()).isEqualTo("Equipe Ada");
        verify(organizationRepository).save(any(Organization.class));
        verify(memberRepository).save(any(OrganizationMember.class));
    }

    @Test
    void create_shouldThrowWhenUserNotFound() {
        when(userRepository.findByEmail("inconnu@example.com")).thenReturn(Optional.empty());

        CreateOrganizationRequest request = new CreateOrganizationRequest();
        request.setName("Test");

        assertThatThrownBy(() -> organizationService.create(request, "inconnu@example.com"))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void findById_shouldThrowWhenUserIsNotMember() {
        Organization organization = new Organization("Equipe Ada", null, user);
        when(organizationRepository.findById(1L)).thenReturn(Optional.of(organization));
        when(userRepository.findByEmail("bob@example.com"))
            .thenReturn(Optional.of(new User("bob@example.com", "hashed", "Bob")));
        when(memberRepository.existsByOrganizationIdAndUserId(any(), any())).thenReturn(false);

        assertThatThrownBy(() -> organizationService.findById(1L, "bob@example.com"))
            .isInstanceOf(AccessDeniedException.class);
    }
}
