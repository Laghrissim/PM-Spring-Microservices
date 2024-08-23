package ma.appsegov.solutionservice.DTO;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ma.appsegov.solutionservice.model.Solution;
import ma.appsegov.solutionservice.model.enums.EtatRequete;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class RequestDTO {

    private String description;

    private String etat;

    private String service;

    private LocalDate date_validation;

    private LocalDate date_creation;

    private Long solution_id;

    private Long user_id;

    private Long contact_id;

    private Long project_id;
}
