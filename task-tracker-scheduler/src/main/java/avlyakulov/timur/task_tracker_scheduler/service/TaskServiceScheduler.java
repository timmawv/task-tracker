package avlyakulov.timur.task_tracker_scheduler.service;

import avlyakulov.timur.dto.DailyReportDto;
import avlyakulov.timur.task_tracker_scheduler.dto.UserTaskDto;
import avlyakulov.timur.task_tracker_scheduler.entity.User;
import avlyakulov.timur.task_tracker_scheduler.mapper.TaskMapper;
import avlyakulov.timur.task_tracker_scheduler.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceScheduler {

    private final UserRepository userRepository;
    private final TaskMapper taskMapper;
    private final KafkaTemplate<String, DailyReportDto> kafkaTemplate;
    @Value("${spring.kafka.producer.topic}")
    private String emailTopic;

    @Scheduled(cron = "0 0 0 * * *")
    public void makeReport4Users() {
        List<User> users = userRepository.findAllUsers();
        List<UserTaskDto> listDto = taskMapper.toListDto(users);
        int a = 123;
    }


}