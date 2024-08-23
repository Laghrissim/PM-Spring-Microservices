package ma.appsegov.solutionservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolutionDTO {
    private Long id;
    private String name;
    private String description;
    private String website;
    private String picture;
    private String video;
    private int requestCount;
    private boolean active;
    private String status="user";


}