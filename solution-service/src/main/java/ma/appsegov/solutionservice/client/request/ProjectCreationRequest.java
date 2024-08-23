package ma.appsegov.solutionservice.client.request;

import lombok.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCreationRequest {

    private String name; // solution name
    private String description; // request description
    private Long client_id;
    private Long contact_id;
    private Long solution_id;
    public void setDescriptionFromContactAndSolution(String contactName, String solutionName) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(new Date());
        this.description = contactName + " - " + solutionName + " - " + formattedDate;
    }
}
