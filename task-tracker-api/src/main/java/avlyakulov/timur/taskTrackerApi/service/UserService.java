package avlyakulov.timur.taskTrackerApi.service;

import avlyakulov.timur.taskTrackerApi.config.security.UserAuthProvider;
import avlyakulov.timur.taskTrackerApi.dto.SignInDto;
import avlyakulov.timur.taskTrackerApi.dto.SignUpDto;
import avlyakulov.timur.taskTrackerApi.dto.UserDto;
import avlyakulov.timur.taskTrackerApi.entity.User;
import avlyakulov.timur.taskTrackerApi.exception.AppException;
import avlyakulov.timur.taskTrackerApi.exception.AppExceptionMessage;
import avlyakulov.timur.taskTrackerApi.mapper.UserMapper;
import avlyakulov.timur.taskTrackerApi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
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

    @Transactional
    public UserDto register(SignUpDto signUpDto) {
        User user = userMapper.toEntity(signUpDto);
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        try {
            User savedUser = userRepository.save(user);
            return userMapper.toDto(savedUser);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(AppExceptionMessage.USER_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
        }
    }
}
