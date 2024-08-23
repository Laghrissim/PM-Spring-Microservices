package ma.appsegov.solutionservice.DTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class AccountDTO {
    private LocalDate creation_date;
    private LocalDate activation_date;

    private boolean isActive;

    private String picture;
}
