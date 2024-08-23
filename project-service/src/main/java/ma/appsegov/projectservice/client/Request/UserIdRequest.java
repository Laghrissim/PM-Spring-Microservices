package ma.appsegov.projectservice.client.Request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class UserIdRequest {
    private Long id;

    public UserIdRequest(Long userid) {
        this.id = userid;
    }
}