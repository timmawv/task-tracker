package avlyakulov.timur.task_tracker_amqp.service;

import avlyakulov.timur.dto.DailyReportDto;
import avlyakulov.timur.dto.WelcomeLetterDto;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Setter(onMethod_ = @__({@Autowired}))
    private JavaMailSender javaMailSender;

    public void sendEmail(WelcomeLetterDto welcomeLetterDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(welcomeLetterDto.getEmail());
        message.setSubject(welcomeLetterDto.getTitle());
        message.setText(welcomeLetterDto.getDescription());
        javaMailSender.send(message);
    }

    public void sendEmail(DailyReportDto dailyReportDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(dailyReportDto.getEmail());
        message.setSubject(dailyReportDto.getTitle());
        message.setText(dailyReportDto.getDescription());
        javaMailSender.send(message);
    }
}
