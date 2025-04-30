package avlyakulov.timur.taskTrackerApi.dto;

import avlyakulov.timur.taskTrackerApi.util.TaskState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskUpdateStateDto {
    TaskState taskState;
}
