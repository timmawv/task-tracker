package avlyakulov.timur.taskTrackerApi.util;

import avlyakulov.timur.dto.WelcomeLetterDto;
import io.micrometer.common.util.StringUtils;

public class WelcomeLetterUtil {

    private static final String title = "New registration";
    private static final String description = "Welcome %s to the web site. Thank your for your registration. We hope your using will be exciting.";

    public static WelcomeLetterDto makeWelcomeLetter(String email) {
        if (StringUtils.isNotBlank(email)) {
            return WelcomeLetterDto.builder()
                    .email(email)
                    .title(title)
                    .description(description.formatted(email))
                    .build();
        }
        return new WelcomeLetterDto();
    }
}
