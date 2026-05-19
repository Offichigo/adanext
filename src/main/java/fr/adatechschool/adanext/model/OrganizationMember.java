package fr.adatechschool.adanext.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "organization_members",
    uniqueConstraints = @UniqueConstraint(columnNames = {"organization_id", "user_id"})
)
public class OrganizationMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private LocalDateTime joinedAt = LocalDateTime.now();

    public enum Role {
        OWNER,
        MEMBER
    }

    public OrganizationMember() {}

    public OrganizationMember(Organization organization, User user, Role role) {
        this.organization = organization;
        this.user = user;
        this.role = role;
    }

    public Long getId() { return id; }
    public Organization getOrganization() { return organization; }
    public User getUser() { return user; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public LocalDateTime getJoinedAt() { return joinedAt; }
}
