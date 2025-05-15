package avlyakulov.timur.task_tracker_amqp.handler;

import avlyakulov.timur.dto.DailyReportDto;
import avlyakulov.timur.dto.WelcomeLetterDto;
import avlyakulov.timur.task_tracker_amqp.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class EmailSenderHandler {

    private final EmailService emailService;

    //todo make logs information
    @KafkaListener(topics = "${spring.kafka.consumer.topic}", containerFactory = "kafkaListenerContainerFactory")
    public void handleWelcomeMessage(@Payload WelcomeLetterDto welcomeLetterDto) {
        log.info("Received event: {}", welcomeLetterDto.getDescription());
        emailService.sendEmail(welcomeLetterDto);
    }

    @KafkaListener(topics = "${spring.kafka.consumer.topic}", containerFactory = "kafkaListenerContainerFactory")
    public void handleDailyReportMessages(@Payload DailyReportDto dailyReportDto) {
        log.info("Received event: {}", dailyReportDto.getTitle());
        emailService.sendEmail(dailyReportDto);
    }
}