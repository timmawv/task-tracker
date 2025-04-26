package avlyakulov.timur.taskTrackerApi.controller;

import avlyakulov.timur.taskTrackerApi.dto.TaskDto;
import avlyakulov.timur.taskTrackerApi.dto.UserDto;
import avlyakulov.timur.taskTrackerApi.service.TaskService;
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

    @GetMapping
    public ResponseEntity<List<TaskDto>> getListTask(@AuthenticationPrincipal UserDto userDto) {
        List<TaskDto> tasks = taskService.findTasksByUserId(userDto.getId());
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto, @AuthenticationPrincipal UserDto userDto) {
        TaskDto taskByUserId = taskService.createTaskByUserId(taskDto, userDto.getId());
        return ResponseEntity.ok(taskByUserId);
    }
}
