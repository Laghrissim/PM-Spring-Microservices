package ma.appsegov.projectservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReadReceiptRequest {

    private String channel;
    private String username;


}
