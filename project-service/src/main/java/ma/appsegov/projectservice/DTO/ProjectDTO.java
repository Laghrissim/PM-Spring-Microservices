package ma.appsegov.projectservice.DTO;

import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.*;
import ma.appsegov.projectservice.model.KanbanBoard;
import ma.appsegov.projectservice.model.Task;
import ma.appsegov.projectservice.model.Team;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectDTO {

    private Long id;
    private String name;

    private String description;

    private String stage ="Arriéré";

    private Long manager_id;

    private Long client_id;

    private Long contact_id;

    private Long solution_id;

//    private KanbanBoard kanbanBoard;

    private Date creationDate;

    private List<Long> subscriberIds;



}
