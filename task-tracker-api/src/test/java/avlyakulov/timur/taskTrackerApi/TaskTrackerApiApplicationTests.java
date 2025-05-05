package avlyakulov.timur.taskTrackerApi;

import avlyakulov.timur.taskTrackerApi.config.security.UserAuthProvider;
import avlyakulov.timur.taskTrackerApi.dto.UserDto;
import avlyakulov.timur.taskTrackerApi.exception.AppExceptionMessage;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

class TaskTrackerApiApplicationTests extends AbstractTestApplication {

    @LocalServerPort
    private Integer port;

    @Autowired
    private UserAuthProvider userAuthProvider;

    private String token;

    private final UserDto userDto = UserDto.builder().email("email@gmail.com").id(1L).build();

    @BeforeAll
    void setUp() {
        RestAssured.port = port;
        token = userAuthProvider.createToken(userDto);
    }

    @Test
    public void whenUserLoggedAndWrongUrl_getBadRequest() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .header("Authorization", "Bearer " + token)
                .get("/wrong")
                .then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("message", equalTo(AppExceptionMessage.URL_NOT_FOUND));
    }

    @Test
    public void whenUserNotLoggedAndWrongUrl_getForbidden() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/wrong")
                .then()
                .statusCode(403);
    }
}