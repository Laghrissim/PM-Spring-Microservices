package ma.appsegov.authservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolutionDTO {
    private String name;
    private String website;
    private String picture;
    private String video;
    private boolean active;

}