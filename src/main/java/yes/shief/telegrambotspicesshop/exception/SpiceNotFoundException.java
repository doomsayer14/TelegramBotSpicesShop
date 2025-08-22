package yes.shief.telegrambotspicesshop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SpiceNotFoundException extends RuntimeException {
    public SpiceNotFoundException(String message) {
        super(message);
    }
}