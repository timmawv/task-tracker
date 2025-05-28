package avlyakulov.timur.taskTrackerApi.config.security;

import avlyakulov.timur.taskTrackerApi.dto.UserDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUserProvider {

    private UserDto getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDto userDto) {
            return userDto;
        }
        throw new RuntimeException("No authenticated user found");
    }

    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }
}