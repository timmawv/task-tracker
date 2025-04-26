package avlyakulov.timur.taskTrackerApi.dto;

import avlyakulov.timur.taskTrackerApi.util.TaskState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDto {

    private String id;

    private String title;

    private String description;

    private TaskState taskState;

    private Boolean isCompleted;

    private LocalDateTime createdAt;

    private LocalDateTime finishedAt;
}