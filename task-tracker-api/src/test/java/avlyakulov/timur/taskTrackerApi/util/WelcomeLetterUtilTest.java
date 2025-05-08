package avlyakulov.timur.taskTrackerApi.util;

import avlyakulov.timur.taskTrackerApi.dto.WelcomeLetterDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WelcomeLetterUtilTest {


    @Test
    void makeWelcomeLetter() {
        WelcomeLetterDto welcomeLetterDto = WelcomeLetterUtil.makeWelcomeLetter("timur@gmail.com");

        assertEquals("timur@gmail.com", welcomeLetterDto.getEmail());
        assertEquals("New registration", welcomeLetterDto.getTitle());
        assertEquals("Welcome timur@gmail.com to the web site. Thank your for your registration. We hope your using will be exciting.", welcomeLetterDto.getDescription());
    }
}