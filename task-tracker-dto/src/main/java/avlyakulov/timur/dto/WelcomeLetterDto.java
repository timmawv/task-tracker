package avlyakulov.timur.dto;

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