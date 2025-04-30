package avlyakulov.timur.taskTrackerApi.config;

import avlyakulov.timur.taskTrackerApi.dto.ErrorDto;
import avlyakulov.timur.taskTrackerApi.exception.AppException;
import avlyakulov.timur.taskTrackerApi.util.ErrorsParsingBindingResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = {AppException.class})
    public ResponseEntity<ErrorDto> handleException(AppException ex) {
        return ResponseEntity.status(ex.getHttpStatus())
                .body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationExceptions(BindingResult bindingResult) {
        String errorMessage = ErrorsParsingBindingResult.parseErrors(bindingResult);
        ErrorDto errorDto = new ErrorDto(errorMessage);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorDto> handleException(Exception exception) {
//        ErrorDto errorDto = new ErrorDto(exception.getMessage());
//        log.error(errorDto.getMessage());
//        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
//    }
}
