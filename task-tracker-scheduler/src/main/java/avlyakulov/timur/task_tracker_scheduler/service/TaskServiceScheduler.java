package avlyakulov.timur.task_tracker_scheduler.service;

import avlyakulov.timur.dto.DailyReportDto;
import avlyakulov.timur.task_tracker_scheduler.dto.TaskDto;
import avlyakulov.timur.task_tracker_scheduler.dto.UserTaskDto;
import avlyakulov.timur.task_tracker_scheduler.entity.User;
import avlyakulov.timur.task_tracker_scheduler.mapper.TaskMapper;
import avlyakulov.timur.task_tracker_scheduler.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceScheduler {

    private final UserRepository userRepository;
    private final TaskMapper taskMapper;
    private final KafkaTemplate<String, DailyReportDto> kafkaTemplate;
    @Value("${spring.kafka.producer.topic}")
    private String emailTopic;
    private final String dailyReportTitle = "Tasks daily report";
    private final String descriptionNotFinishedTasks = "By this day, you have the following not finished tasks: \n";
    private final String descriptionFinishedTasks = "Congratulations! Today, you have finished the following tasks: \n";


    @Scheduled(cron = "0 0 0 * * *")
    public void makeReport4Users() {
        List<User> users = userRepository.findAllUsers();
        List<UserTaskDto> userTasks = taskMapper.toListDto(users);
        List<DailyReportDto> dailyReports = makeDailyReports(userTasks);
        dailyReports.forEach(dailyReport -> kafkaTemplate.send(emailTopic, dailyReport));
    }

    public List<DailyReportDto> makeDailyReports(List<UserTaskDto> userTasks) {
        return userTasks.stream()
                .map(user -> {
                    String notFinishedTasks = findNotFinishedTasks(user);
                    String finishedTasks = findFinishedTasks(user);
                    return DailyReportDto.builder()
                            .email(user.getEmail())
                            .title(dailyReportTitle)
                            .description(notFinishedTasks.concat(finishedTasks))
                            .build();
                })
                .toList();
    }

    public String findNotFinishedTasks(UserTaskDto userTask) {
        StringBuilder descNotFinishedTasks = new StringBuilder(descriptionNotFinishedTasks);
        List<TaskDto> notFinishedTasks = userTask.getTasks().stream()
                .filter(task -> !task.getIsCompleted())
                .limit(5)
                .toList();
        if (!notFinishedTasks.isEmpty()) {
            notFinishedTasks.forEach(task -> descNotFinishedTasks.append("• ".concat(task.getTitle()).concat("\n")));;
            return descNotFinishedTasks.toString();
        }
        return StringUtils.EMPTY;
    }

    public String findFinishedTasks(UserTaskDto userTask) {
        StringBuilder descFinishedTasks = new StringBuilder(descriptionFinishedTasks);
        LocalDate previousDay = LocalDate.now();
        List<TaskDto> finishedTasks = userTask.getTasks().stream()
                .filter(task -> (task.getIsCompleted() && task.getFinishedAt().toLocalDate().isEqual(previousDay)))
                .limit(5)
                .toList();
        if (!finishedTasks.isEmpty()) {
            finishedTasks.forEach(task -> descFinishedTasks.append("• ".concat(task.getTitle()).concat("\n")));
            return descFinishedTasks.toString();
        }
        return StringUtils.EMPTY;
    }
}