package avlyakulov.timur.task_tracker_scheduler.dto;

import avlyakulov.timur.task_tracker_scheduler.util.TaskState;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    private String id;
    private String title;
    private String description;
    private TaskState taskState;
    private Boolean isCompleted;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
}