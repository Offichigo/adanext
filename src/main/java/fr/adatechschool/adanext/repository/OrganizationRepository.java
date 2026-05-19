package fr.adatechschool.adanext.repository;

import fr.adatechschool.adanext.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    @Query("SELECT o FROM Organization o JOIN o.members m WHERE m.user.id = :userId")
    List<Organization> findAllByMemberId(@Param("userId") Long userId);
}
