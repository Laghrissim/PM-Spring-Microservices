package ma.appsegov.projectservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String stage ="Arriéré";

    private Long manager_id;

    private Long client_id;

    private Long contact_id;

    private Long solution_id;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Team> teams = new ArrayList<>();

    @OneToMany
    private List<Task> tasks = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    private KanbanBoard kanbanBoard;

    @ElementCollection
    private List<Long> subscriberIds;


    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

}