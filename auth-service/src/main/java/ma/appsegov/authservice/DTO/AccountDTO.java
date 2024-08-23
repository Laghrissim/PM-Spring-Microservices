package ma.appsegov.authservice.DTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class AccountDTO {
    private LocalDate creation_date;
    private LocalDate activation_date;

    private boolean isActive;

    private boolean isCompany;

    private String picture;
}
