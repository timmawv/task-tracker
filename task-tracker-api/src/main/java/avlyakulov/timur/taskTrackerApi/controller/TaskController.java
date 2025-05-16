package avlyakulov.timur.taskTrackerApi.controller;

import avlyakulov.timur.taskTrackerApi.dto.AppMessageDto;
import avlyakulov.timur.taskTrackerApi.dto.TaskDto;
import avlyakulov.timur.taskTrackerApi.dto.UserDto;
import avlyakulov.timur.taskTrackerApi.service.TaskService;
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
        TaskDto taskResponse = taskService.createTaskByUserId(taskDto, userDto.getId());
        return ResponseEntity.ok(taskResponse);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDto> updateTask(@RequestBody @Valid TaskDto taskDto, @PathVariable String taskId) {
        TaskDto updatedTask = taskService.updateTask(taskDto, taskId);
        return ResponseEntity.ok(updatedTask);
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<TaskDto> updateTaskState(@RequestBody TaskDto taskDto, @PathVariable String taskId) {
        TaskDto updatedTaskDto = taskService.updateTaskState(taskId, taskDto.getTaskState());
        return ResponseEntity.ok(updatedTaskDto);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<AppMessageDto> deleteTask(@PathVariable String taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok(new AppMessageDto("Task was successfully deleted"));
    }
}
