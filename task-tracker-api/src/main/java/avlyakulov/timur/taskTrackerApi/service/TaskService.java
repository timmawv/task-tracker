package avlyakulov.timur.taskTrackerApi.service;

import avlyakulov.timur.taskTrackerApi.dto.TaskDto;
import avlyakulov.timur.taskTrackerApi.entity.Task;
import avlyakulov.timur.taskTrackerApi.entity.User;
import avlyakulov.timur.taskTrackerApi.exception.AppException;
import avlyakulov.timur.taskTrackerApi.exception.AppExceptionMessage;
import avlyakulov.timur.taskTrackerApi.mapper.TaskMapper;
import avlyakulov.timur.taskTrackerApi.repository.TaskRepository;
import avlyakulov.timur.taskTrackerApi.util.TaskState;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    public List<TaskDto> findTasksByUserId(Long userId) {
        return taskMapper.toListDto(taskRepository.findAllByUserId(userId));
    }

    @Transactional
    public TaskDto createTaskByUserId(TaskDto taskDto, Long userId) {
        Task task = taskMapper.toEntity(taskDto);
        task.setId(UUID.randomUUID().toString());
        task.setCreatedAt(LocalDateTime.now());
        task.setTaskState(TaskState.NOT_STARTED);
        task.setIsCompleted(false);
        task.setOwner(new User(userId));
        taskRepository.save(task);
        return taskMapper.toDto(task);
    }

    @Transactional
    public TaskDto updateTask(TaskDto taskDto, String taskId) {
        Optional<Task> task = taskRepository.findById(taskId);
        Task updatedTask = task
                .map(t -> {
                    t.setTitle(taskDto.getTitle());
                    t.setDescription(taskDto.getDescription());
                    return t;
                })
                .orElseThrow(() -> new AppException(AppExceptionMessage.TASK_NOT_FOUND, HttpStatus.NOT_FOUND));
        return taskMapper.toDto(taskRepository.save(updatedTask));
    }

    @Transactional
    public void updateTaskState(Long userId, String taskId, TaskState taskState) {
        taskRepository.updateTaskState(userId, taskId, taskState);
    }

    @Transactional
    public void deleteTask(String taskId) {
        taskRepository.deleteTask(taskId);
    }
}