package avlyakulov.timur.taskTrackerApi.controller.auth;

import avlyakulov.timur.taskTrackerApi.entity.User;
import avlyakulov.timur.taskTrackerApi.exception.AppExceptionMessage;
import avlyakulov.timur.taskTrackerApi.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNot.not;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isNotNull;

@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeAll
    void setUp() {
        RestAssured.port = port;
    }

    @BeforeEach
    void setUpEach() {
        user = User.builder().email("example@gmail.com").password("$2a$10$.5mTYBHWfWKojGDR7IVylOEBc0sGvvQVpN/8.jcC8UJ9nUbz6MU4q").build();
    }

    @AfterEach
    void tearUp() {
        userRepository.deleteAll();
    }

    @AfterAll
    void tearAll() {
        userRepository.deleteAll();
    }

    @Test
    public void login_whenRightCredentials_shouldAccess() {
        userRepository.save(user);
        int a = 123;
        given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "email" : "example@gmail.com",
                              "password" : 123
                            }
                        """
                )
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", notNullValue())
                .body("email", equalTo("example@gmail.com"))
                .body("token", not(isEmptyOrNullString()));
    }

    @Test
    public void login_whenWrongCredentials_shouldNotAccess() {
        userRepository.save(user);

        given()
                .contentType(ContentType.JSON)
                .body("""
                            {
                              "email" : "example@gmail.com",
                              "password" : 1234
                            }
                        """
                )
                .when()
                .post("/login")
                .then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("message", equalTo(AppExceptionMessage.CRED_NOT_CORRECT));
    }
}