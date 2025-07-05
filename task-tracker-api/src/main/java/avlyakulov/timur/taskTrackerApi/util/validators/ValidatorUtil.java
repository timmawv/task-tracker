package avlyakulov.timur.taskTrackerApi.util.validators;

import avlyakulov.timur.taskTrackerApi.util.ErrorsParsingBindingResult;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.function.Function;

@Component
public class ValidatorUtil {

    public <T, R extends Errors> void validate(T value, Function<T, R> validator) {
        R object = validator.apply(value);
        ErrorsParsingBindingResult.checkErrors(object);
    }
}