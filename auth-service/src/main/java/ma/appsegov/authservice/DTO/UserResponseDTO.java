package ma.appsegov.authservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class UserResponseDTO {
    private Long userId;
    private String accessToken;
    private String refreshToken;
}
