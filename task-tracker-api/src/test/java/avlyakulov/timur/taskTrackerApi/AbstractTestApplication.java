package avlyakulov.timur.taskTrackerApi;

import avlyakulov.timur.taskTrackerApi.dto.TaskDto;
import avlyakulov.timur.taskTrackerApi.entity.User;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AbstractTestApplication {

    public User getDefaultUser() {
        return User.builder().email("example@gmail.com").password("$2a$10$.5mTYBHWfWKojGDR7IVylOEBc0sGvvQVpN/8.jcC8UJ9nUbz6MU4q").build();
    }

    public List<TaskDto> getDefaultTasks() {
        return List.of(
                TaskDto.builder().title("Buy a sofa").description("finish till 27 october").build(),
                TaskDto.builder().title("Buy a bread").description("In the morning go to the store").build()
        );
    }
}