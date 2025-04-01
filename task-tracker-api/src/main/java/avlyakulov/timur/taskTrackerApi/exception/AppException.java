package avlyakulov.timur.taskTrackerApi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppException extends RuntimeException {

    private String message;
    private HttpStatus httpStatus;
}
