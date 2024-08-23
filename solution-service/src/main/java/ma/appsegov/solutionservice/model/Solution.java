package ma.appsegov.solutionservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Solution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String website;
    private String picture;
    private String description  ;
    private String video;
    @Column(columnDefinition = "varchar(255) default 'user'")
    private String status="user";
    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean active = true;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "solution")
    private List<Request> requests;

    @ElementCollection
    private List<Long> contactIds;
    @Transient
    public Long getTaskNumber() {
        if (requests != null) {
            return (long) requests.size();
        } else {
            return 0L;
        }
    }
}
