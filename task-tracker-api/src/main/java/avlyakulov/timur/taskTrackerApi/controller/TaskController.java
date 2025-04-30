package avlyakulov.timur.taskTrackerApi.controller;

import avlyakulov.timur.taskTrackerApi.dto.AppMessageDto;
import avlyakulov.timur.taskTrackerApi.dto.TaskDto;
import avlyakulov.timur.taskTrackerApi.dto.TaskUpdateStateDto;
import avlyakulov.timur.taskTrackerApi.dto.UserDto;
import avlyakulov.timur.taskTrackerApi.service.TaskService;
import avlyakulov.timur.taskTrackerApi.util.TaskState;
import ch.qos.logback.core.model.AppenderModel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    //todo подумать куда вынести этот принципал, забирает место
    @GetMapping
    public ResponseEntity<List<TaskDto>> getListTask(@AuthenticationPrincipal UserDto userDto) {
        List<TaskDto> tasks = taskService.findTasksByUserId(userDto.getId());
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<TaskDto> createTask(@RequestBody @Valid TaskDto taskDto, @AuthenticationPrincipal UserDto userDto) {
        TaskDto taskByUserId = taskService.createTaskByUserId(taskDto, userDto.getId());
        return ResponseEntity.ok(taskByUserId);
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<AppMessageDto> updateTaskState(@RequestBody TaskUpdateStateDto taskUpdateStateDto, @PathVariable String taskId, @AuthenticationPrincipal UserDto userDto) {
        taskService.updateTaskState(userDto.getId(), taskId, taskUpdateStateDto.getTaskState());
        return ResponseEntity.ok(new AppMessageDto("Task was successfully updated"));
    }
}
