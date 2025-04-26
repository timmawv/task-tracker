package avlyakulov.timur.taskTrackerApi.mapper;

import avlyakulov.timur.taskTrackerApi.dto.TaskDto;
import avlyakulov.timur.taskTrackerApi.entity.Task;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface TaskMapper {

    List<TaskDto> toListDto(List<Task> tasks);

    Task toEntity(TaskDto taskDto);

    TaskDto toDto(Task task);
}
