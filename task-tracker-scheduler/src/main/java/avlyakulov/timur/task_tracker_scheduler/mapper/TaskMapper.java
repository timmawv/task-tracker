package avlyakulov.timur.task_tracker_scheduler.mapper;

import avlyakulov.timur.task_tracker_scheduler.dto.TaskDto;
import avlyakulov.timur.task_tracker_scheduler.dto.UserTaskDto;
import avlyakulov.timur.task_tracker_scheduler.entity.Task;
import avlyakulov.timur.task_tracker_scheduler.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface TaskMapper {

    List<UserTaskDto> toListDto(List<User> users);

    UserTaskDto mapToDto(User user);

    TaskDto toDto(Task task);
}