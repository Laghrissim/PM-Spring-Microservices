package ma.appsegov.projectservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SolutionNotFoundException extends RuntimeException {
    public SolutionNotFoundException(String message) {
        super(message);
    }
}
