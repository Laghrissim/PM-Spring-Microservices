package ma.appsegov.authservice.DTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ContactDTO {
    private Long id;

    private String name;
    private String picture;
    private String website;
    private String address;
    private String mobile;
    private String email;
}
