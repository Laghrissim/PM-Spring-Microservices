package ma.appsegov.projectservice.repository;

import ma.appsegov.projectservice.model.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

//    Page<Message> findAllByChannel(String channel, Pageable pageable);
    List<Message> findAllByProjectId(Long projectId);



    @Modifying
    @Query(value = "update message set read_date = now() "
            + " where channel = ?1 and sender = ?2 and readDate is null", nativeQuery = true)
    void sendReadReceipt(String channel, String username);
}