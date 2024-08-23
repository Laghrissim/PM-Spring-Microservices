package ma.appsegov.solutionservice.repository;

import ma.appsegov.solutionservice.model.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SolutionRepository extends JpaRepository<Solution,Long> {
    Solution findDistinctById(Long id);

    @Query("SELECT s FROM Solution s JOIN s.requests r WHERE r.contact_id =?1")
    Solution findSolutionByRequestContactId(Long contact_id);
}
