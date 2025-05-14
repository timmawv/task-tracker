package avlyakulov.timur.task_tracker_scheduler.repository;

import avlyakulov.timur.task_tracker_scheduler.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("Select u from User u join fetch u.tasks")
    List<User> findAllUsers();
}