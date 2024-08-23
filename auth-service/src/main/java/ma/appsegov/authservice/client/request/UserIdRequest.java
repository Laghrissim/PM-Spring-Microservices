package ma.appsegov.authservice.client.request;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ma.appsegov.authservice.model.User;


@Data
@RequiredArgsConstructor
public class UserIdRequest {
    private Long id;
}
