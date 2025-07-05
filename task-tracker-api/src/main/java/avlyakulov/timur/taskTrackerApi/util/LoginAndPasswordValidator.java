package avlyakulov.timur.taskTrackerApi.util;

import avlyakulov.timur.taskTrackerApi.dto.SignUpDto;
import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.SimpleErrors;
import org.springframework.validation.Validator;

@Component
public class LoginAndPasswordValidator implements Validator {

    private final String LOGIN_FIELD = "email";

    private final String LOGIN_EMAIL_FIELD = "email";

    private final LoginTypeValidatorEnum type = LoginTypeValidatorEnum.EMAIL;

    private final String PASSWORD_FIELD = "password";

    private final String CONFIRM_PASSWORD_FIELD = "confirmPassword";

    private final String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private final String emailTypeRegex = ".*\\.[a-zA-Z0-9]{2,}";

    private final String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$";

    @Override
    public boolean supports(Class<?> clazz) {
        return SignUpDto.class.equals(clazz);
    }

    @Override
    public Errors validateObject(Object target) {
        SignUpDto signUpDto = (SignUpDto) target;
        Errors errors = new SimpleErrors(signUpDto);
        String login = signUpDto.getEmail();
        String password = signUpDto.getPassword();
        String confirmPassword = signUpDto.getConfirmPassword();

        if (StringUtils.isBlank(login)) {
            rejectValue(LOGIN_FIELD, "Your login can't be null or empty. Enter valid login", errors);
            return errors;
        }

        switch (type) {
            case LOGIN -> validateLoginAsLogin(login, errors);
            case EMAIL -> validateLoginAsEmail(login, errors);
        }

        if (login.length() < 2 || login.length() > 30) {
            rejectValue(LOGIN_FIELD, "Your login length has to be from 2 to 30 symbols", errors);
            return errors;
        }

        if (!(password.length() >= 6 && password.length() <= 25)) {
            rejectValue(PASSWORD_FIELD, "The length of password has to be from 6 to 25", errors);
            return errors;
        }

        if (!password.matches(passwordRegex)) {
            rejectValue(PASSWORD_FIELD, "Your password must contain one capital letter one small letter and one number", errors);
            return errors;
        }

        Zxcvbn zxcvbn = new Zxcvbn();
        Strength strength = zxcvbn.measure(password);
        if (strength.getScore() < 3) {
            String suggestions = strength.getFeedback().getSuggestions().toString();
            suggestions = suggestions.substring(1, suggestions.length() - 1);
            rejectValue(PASSWORD_FIELD, "Your password is to easy. Here is some suggestions to help you : ".concat(suggestions), errors);
            return errors;
        }
        if (!password.equals(confirmPassword))
            rejectValue(CONFIRM_PASSWORD_FIELD, "The passwords aren't same. Please enter the same passwords", errors);

        return errors;
    }

    @Override
    public void validate(Object target, Errors errors) {

    }

    private void validateLoginAsLogin(String login, Errors errors) {
        if (login.contains("@") && !login.matches(emailRegex))
            rejectValue(LOGIN_FIELD, "Your email isn't valid. Please enter valid email", errors);
    }

    private void validateLoginAsEmail(String login, Errors errors) {
        if (!login.matches(emailRegex))
            rejectValue(LOGIN_FIELD, "Your email isn't valid. Please enter valid email", errors);
    }

    private void rejectValue(String nameField, String message, Errors errors) {
        errors.rejectValue(nameField, "", message);
    }
}
