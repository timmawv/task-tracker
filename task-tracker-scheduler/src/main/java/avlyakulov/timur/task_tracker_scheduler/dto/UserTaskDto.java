package avlyakulov.timur.task_tracker_scheduler.dto;

import avlyakulov.timur.task_tracker_scheduler.util.TaskState;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTaskDto {

    private String email;
    private List<TaskDto> tasks;
}
