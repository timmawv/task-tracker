package avlyakulov.timur.taskTrackerApi.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;

    private String email;

    private String token;
}
