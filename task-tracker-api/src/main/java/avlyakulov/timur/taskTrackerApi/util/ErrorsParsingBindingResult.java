package avlyakulov.timur.taskTrackerApi.util;

import avlyakulov.timur.taskTrackerApi.exception.AppException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

public class ErrorsParsingBindingResult {

    public static void checkErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = parseErrors(bindingResult);
            throw new AppException(errorMessage, HttpStatus.BAD_REQUEST);
        }
    }

    public static String parseErrors(BindingResult bindingResult) {
        List<ObjectError> errors = bindingResult.getAllErrors();
        return errors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("\n"));
    }
}
