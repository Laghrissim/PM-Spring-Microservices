package ma.appsegov.solutionservice.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.appsegov.solutionservice.model.enums.EtatRequete;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EtatRequete etat = EtatRequete.NON_VALIDE;

    private String service;

    private String description;

    private LocalDate date_validation;

    private LocalDate date_creation;

    private Long user_id;
    private Long contact_id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Solution solution;

}
