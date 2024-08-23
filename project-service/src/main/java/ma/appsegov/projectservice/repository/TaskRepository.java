package ma.appsegov.projectservice.repository;

import ma.appsegov.projectservice.model.Project;
import ma.appsegov.projectservice.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
