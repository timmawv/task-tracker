package avlyakulov.timur.taskTrackerApi.repository;

import avlyakulov.timur.taskTrackerApi.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

    @Query("select t from Task t where t.owner.id = ?1 and t.taskState != 'DELETED'")
    List<Task> findAllByUserId(Long userId);
}
