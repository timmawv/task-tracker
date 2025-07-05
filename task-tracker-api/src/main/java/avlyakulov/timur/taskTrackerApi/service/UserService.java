package avlyakulov.timur.taskTrackerApi.service;

import avlyakulov.timur.dto.WelcomeLetterDto;
import avlyakulov.timur.taskTrackerApi.config.security.UserAuthProvider;
import avlyakulov.timur.taskTrackerApi.dto.*;
import avlyakulov.timur.taskTrackerApi.entity.User;
import avlyakulov.timur.taskTrackerApi.exception.AppException;
import avlyakulov.timur.taskTrackerApi.exception.AppExceptionMessage;
import avlyakulov.timur.taskTrackerApi.mapper.UserMapper;
import avlyakulov.timur.taskTrackerApi.repository.UserRepository;
import avlyakulov.timur.taskTrackerApi.util.validators.LoginAndPasswordValidator;
import avlyakulov.timur.taskTrackerApi.util.WelcomeLetterUtil;
import avlyakulov.timur.taskTrackerApi.util.validators.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final UserAuthProvider userAuthProvider;
    private final PasswordEncoder passwordEncoder;
    private final ValidatorUtil validatorUtil;
    private final LoginAndPasswordValidator loginAndPasswordValidator;
    private final KafkaTemplate<String, WelcomeLetterDto> kafkaTemplate;
    @Value("${spring.kafka.producer.topic}")
    private String emailTopic;

    public UserDto login(SignInDto signInDto) {
        User user = userRepository.findByEmail(signInDto.getEmail())
                .orElseThrow(() -> new AppException(AppExceptionMessage.CRED_NOT_CORRECT, HttpStatus.BAD_REQUEST));

        if (passwordEncoder.matches(signInDto.getPassword(), user.getPassword())) {
            UserDto userDto = userMapper.toDto(user);
            userDto.setToken(userAuthProvider.createToken(userDto));
            return userDto;
        }

        throw new AppException(AppExceptionMessage.CRED_NOT_CORRECT, HttpStatus.BAD_REQUEST);
    }

    @Transactional("transactionManager")
    public UserDto register(SignUpDto signUpDto) {
        validatorUtil.validate(signUpDto, loginAndPasswordValidator::validateObject);
        User user = userMapper.toEntity(signUpDto);
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        try {
            User savedUser = userRepository.save(user);
            WelcomeLetterDto welcomeLetterDto = WelcomeLetterUtil.makeWelcomeLetter(savedUser.getEmail());
            kafkaTemplate.send(emailTopic, welcomeLetterDto);
            return userMapper.toDto(savedUser);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(AppExceptionMessage.USER_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
        }
    }
}
