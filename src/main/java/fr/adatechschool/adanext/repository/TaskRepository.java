package fr.adatechschool.adanext.repository;

import fr.adatechschool.adanext.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByProjectId(Long projectId);

    List<Task> findAllByProjectIdAndStatus(Long projectId, Task.Status status);
}
