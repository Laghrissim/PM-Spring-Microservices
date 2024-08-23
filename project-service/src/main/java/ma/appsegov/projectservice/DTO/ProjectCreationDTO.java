package ma.appsegov.projectservice.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectCreationDTO {

    private String name; // solution name
    private String description; // request description
    private Long client_id;
    private Long request_id;
}