package fr.adatechschool.adanext.repository;

import fr.adatechschool.adanext.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAllByOrganizationId(Long organizationId);
}
