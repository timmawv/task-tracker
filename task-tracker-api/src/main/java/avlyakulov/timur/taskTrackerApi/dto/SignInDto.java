package avlyakulov.timur.taskTrackerApi.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignInDto {

    @NotEmpty(message = "Field email can't be null or empty")
    private String email;
    @NotEmpty(message = "Field password can't be null or empty")
    private String password;
}