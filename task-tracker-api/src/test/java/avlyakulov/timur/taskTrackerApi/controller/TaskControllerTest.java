package avlyakulov.timur.taskTrackerApi.controller;

import avlyakulov.timur.taskTrackerApi.AbstractTestApplication;
import avlyakulov.timur.taskTrackerApi.config.security.UserAuthProvider;
import avlyakulov.timur.taskTrackerApi.dto.SignInDto;
import avlyakulov.timur.taskTrackerApi.dto.TaskDto;
import avlyakulov.timur.taskTrackerApi.entity.Task;
import avlyakulov.timur.taskTrackerApi.entity.User;
import avlyakulov.timur.taskTrackerApi.exception.AppExceptionMessage;
import avlyakulov.timur.taskTrackerApi.mapper.TaskMapper;
import avlyakulov.timur.taskTrackerApi.mapper.UserMapper;
import avlyakulov.timur.taskTrackerApi.repository.TaskRepository;
import avlyakulov.timur.taskTrackerApi.repository.UserRepository;
import avlyakulov.timur.taskTrackerApi.service.TaskService;
import avlyakulov.timur.taskTrackerApi.service.UserService;
import avlyakulov.timur.taskTrackerApi.util.TaskState;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
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
        //todo hide the password
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
                .body("[0].finishedAt", nullValue())
                .body("[1].id", not(blankOrNullString()))
                .body("[1].title", equalTo("Buy a bread"))
                .body("[1].description", equalTo("In the morning go to the store"))
                .body("[1].taskState", equalTo("NOT_STARTED"))
                .body("[1].isCompleted", equalTo(false))
                .body("[1].createdAt", notNullValue())
                .body("[1].finishedAt", nullValue());
    }

    @Test
    void getListOfTasks_userNoLogged() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/tasks")
                .then()
                .statusCode(403);
    }

    @Test
    void createTask_taskCreated() {
        userRepository.save(user);
        String token = userService.login(new SignInDto(user.getEmail(), "123")).getToken();

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body("""
                            {
                              "title" : "Buy a sofa",
                              "description" : "finish till 27 october"
                            }
                        """
                )
                .when()
                .post("/tasks")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", not(blankOrNullString()))
                .body("title", equalTo("Buy a sofa"))
                .body("description", equalTo("finish till 27 october"))
                .body("taskState", equalTo("NOT_STARTED"))
                .body("isCompleted", equalTo(false))
                .body("createdAt", notNullValue())
                .body("finishedAt", nullValue());

        List<Task> tasks = taskRepository.findAll();
        Task task = tasks.get(0);
        assertThat(tasks, hasSize(1));
        assertThat(task.getId(), not(blankOrNullString()));
        assertThat(task.getTitle(), equalTo("Buy a sofa"));
        assertThat(task.getDescription(), equalTo("finish till 27 october"));
        assertThat(task.getTaskState(), equalTo(TaskState.NOT_STARTED));
        assertThat(task.getIsCompleted(), equalTo(false));
        assertThat(task.getCreatedAt(), notNullValue());
        assertThat(task.getFinishedAt(), nullValue());
    }

    @Test
    void createTask_taskNotCreated_noTitle() {
        userRepository.save(user);
        String token = userService.login(new SignInDto(user.getEmail(), "123")).getToken();

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body("""
                            {
                              "title" : "",
                              "description" : "finish till 27 october"
                            }
                        """
                )
                .when()
                .post("/tasks")
                .then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("message", equalTo("Field title can't be null or empty"));
    }

    @Test
    void updateTask_taskWasUpdated() {
        userRepository.save(user);
        String token = userService.login(new SignInDto(user.getEmail(), "123")).getToken();
        TaskDto taskDto = getDefaultTasks().getFirst();
        TaskDto createdTask = taskService.createTaskByUserId(taskDto, user.getId());

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body("""
                            {
                              "title" : "Buy a cherry",
                              "description" : "finish till middle summer"
                            }
                        """
                )
                .when()
                .put("/tasks/%s".formatted(createdTask.getId()))
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(createdTask.getId()))
                .body("title", equalTo("Buy a cherry"))
                .body("description", equalTo("finish till middle summer"))
                .body("taskState", equalTo("NOT_STARTED"))
                .body("isCompleted", equalTo(false))
                .body("createdAt", notNullValue())
                .body("finishedAt", nullValue());

        List<Task> tasks = taskRepository.findAll();
        Task task = tasks.get(0);
        assertThat(tasks, hasSize(1));
        assertThat(task.getId(), not(blankOrNullString()));
        assertThat(task.getTitle(), equalTo("Buy a cherry"));
        assertThat(task.getDescription(), equalTo("finish till middle summer"));
        assertThat(task.getTaskState(), equalTo(TaskState.NOT_STARTED));
        assertThat(task.getIsCompleted(), equalTo(false));
        assertThat(task.getCreatedAt(), notNullValue());
        assertThat(task.getFinishedAt(), nullValue());
    }

    @Test
    void updateTaskState_taskStateWasUpdated() {
        userRepository.save(user);
        String token = userService.login(new SignInDto(user.getEmail(), "123")).getToken();
        TaskDto taskDto = getDefaultTasks().getFirst();
        TaskDto createdTask = taskService.createTaskByUserId(taskDto, user.getId());

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body("""
                            {
                              "taskState" : "FINISHED"
                            }
                        """
                )
                .when()
                .patch("/tasks/%s".formatted(createdTask.getId()))
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("message", equalTo("Task state was successfully updated"));

        List<Task> tasks = taskRepository.findAll();
        Task task = tasks.get(0);
        assertThat(tasks, hasSize(1));
        assertThat(task.getId(), not(blankOrNullString()));
        assertThat(task.getTitle(), equalTo("Buy a sofa"));
        assertThat(task.getDescription(), equalTo("finish till 27 october"));
        assertThat(task.getTaskState(), equalTo(TaskState.FINISHED));
        assertThat(task.getIsCompleted(), equalTo(true));
        assertThat(task.getCreatedAt(), notNullValue());
        assertThat(task.getFinishedAt(), notNullValue());
    }

    @Test
    void deleteTask_taskWasDeleted() {
        userRepository.save(user);
        String token = userService.login(new SignInDto(user.getEmail(), "123")).getToken();
        TaskDto taskDto = getDefaultTasks().getFirst();
        TaskDto createdTask = taskService.createTaskByUserId(taskDto, user.getId());

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/tasks/%s".formatted(createdTask.getId()))
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("message", equalTo("Task was successfully deleted"));

        List<Task> tasks = taskRepository.findAll();
        Task task = tasks.get(0);
        assertThat(tasks, hasSize(1));
        assertThat(task.getId(), not(blankOrNullString()));
        assertThat(task.getTitle(), equalTo("Buy a sofa"));
        assertThat(task.getDescription(), equalTo("finish till 27 october"));
        assertThat(task.getTaskState(), equalTo(TaskState.DELETED));
        assertThat(task.getIsCompleted(), equalTo(false));
        assertThat(task.getCreatedAt(), notNullValue());
        assertThat(task.getFinishedAt(), nullValue());
    }
}