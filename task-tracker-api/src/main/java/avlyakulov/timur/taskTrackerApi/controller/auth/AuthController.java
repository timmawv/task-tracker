package avlyakulov.timur.taskTrackerApi.controller.auth;

import avlyakulov.timur.taskTrackerApi.config.security.UserAuthProvider;
import avlyakulov.timur.taskTrackerApi.dto.SignInDto;
import avlyakulov.timur.taskTrackerApi.dto.SignUpDto;
import avlyakulov.timur.taskTrackerApi.dto.UserDto;
import avlyakulov.timur.taskTrackerApi.service.UserService;
import avlyakulov.timur.taskTrackerApi.util.ErrorsParsingBindingResult;
import avlyakulov.timur.taskTrackerApi.util.LoginAndPasswordValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserAuthProvider userAuthProvider;
    private final LoginAndPasswordValidator loginAndPasswordValidator;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody @Valid SignInDto signInDto) {
        UserDto userDto = userService.login(signInDto);
        userDto.setToken(userAuthProvider.createToken(userDto));
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody SignUpDto signUpDto, BindingResult bindingResult) {
        loginAndPasswordValidator.validate(signUpDto, bindingResult);
        ErrorsParsingBindingResult.checkErrors(bindingResult);
        UserDto userDto = userService.register(signUpDto);
        return ResponseEntity.created(URI.create("/users/" + userDto.getId())).body(userDto);
    }
}