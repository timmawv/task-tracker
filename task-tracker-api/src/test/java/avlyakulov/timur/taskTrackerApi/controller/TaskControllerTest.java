package avlyakulov.timur.taskTrackerApi.controller;

import avlyakulov.timur.taskTrackerApi.AbstractTestApplication;
import avlyakulov.timur.taskTrackerApi.config.security.UserAuthProvider;
import avlyakulov.timur.taskTrackerApi.dto.SignInDto;
import avlyakulov.timur.taskTrackerApi.dto.TaskDto;
import avlyakulov.timur.taskTrackerApi.entity.User;
import avlyakulov.timur.taskTrackerApi.mapper.UserMapper;
import avlyakulov.timur.taskTrackerApi.repository.TaskRepository;
import avlyakulov.timur.taskTrackerApi.repository.UserRepository;
import avlyakulov.timur.taskTrackerApi.service.TaskService;
import avlyakulov.timur.taskTrackerApi.service.UserService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNot.not;
import static org.junit.jupiter.api.Assertions.*;

class TaskControllerTest extends AbstractTestApplication {

    @LocalServerPort
    private Integer port;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;
    private User user;
    private final List<TaskDto> tasks = getDefaultTasks();

    @BeforeAll
    void setUp() {
        RestAssured.port = port;
        userRepository.deleteAll();
        taskRepository.deleteAll();
        user = getDefaultUser();
    }

    @AfterEach
    void tearUp() {
        userRepository.deleteAll();
        taskRepository.deleteAll();
        user = getDefaultUser();
    }

    @Test
    void getListOfTasks() {
        userRepository.save(user);
        String token = userService.login(new SignInDto(user.getEmail(), "123")).getToken();
        tasks.forEach(task -> taskService.createTaskByUserId(task, user.getId()));

        given()
                .contentType(ContentType.JSON)
                .when()
                .header("Authorization", "Bearer " + token)
                .get("/tasks")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", greaterThan(1))
                .body("[0].id", not(blankOrNullString()))
                .body("[0].title", equalTo("Buy a sofa"))
                .body("[0].description", equalTo("finish till 27 october"))
                .body("[0].taskState", equalTo("NOT_STARTED"))
                .body("[0].isCompleted", equalTo(false))
                .body("[0].createdAt", notNullValue())
                .body("[0].finishedAt", equalTo(null))
                .body("[1].id", not(blankOrNullString()))
                .body("[1].title", equalTo("Buy a bread"))
                .body("[1].description", equalTo("In the morning go to the store"))
                .body("[1].taskState", equalTo("NOT_STARTED"))
                .body("[1].isCompleted", equalTo(false))
                .body("[1].createdAt", notNullValue())
                .body("[1].finishedAt", equalTo(null));
    }
}