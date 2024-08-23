package ma.appsegov.authservice.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class EmailDTO {
    private String email;
}
