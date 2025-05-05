package avlyakulov.timur.taskTrackerApi.dto;

import avlyakulov.timur.taskTrackerApi.util.TaskState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDto {

    private String id;

    @NotBlank(message = "Field title can't be null or empty")
    @Size(max = 30, message = "Max size of the title 30 symbols")
    private String title;

    @Size(max = 100, message = "Max size of the description is 100 symbols")
    private String description;

    private TaskState taskState;

    private Boolean isCompleted;

    private LocalDateTime createdAt;

    private LocalDateTime finishedAt;
}