package ma.appsegov.solutionservice.client.request;

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
