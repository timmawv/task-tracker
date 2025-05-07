package avlyakulov.timur.taskTrackerApi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WelcomeLetterDto {

    private String email;
    private String title;
    private String description;
}
