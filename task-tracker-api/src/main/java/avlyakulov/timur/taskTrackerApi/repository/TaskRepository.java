package avlyakulov.timur.taskTrackerApi.repository;

import avlyakulov.timur.taskTrackerApi.entity.Task;
import avlyakulov.timur.taskTrackerApi.util.TaskState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

    @Query("select t from Task t where t.owner.id = ?1 and t.taskState != 'DELETED'")
    List<Task> findAllByUserId(Long userId);

    @Modifying
    @Query("update Task t set t.taskState = ?3 where t.owner.id = ?1 and t.id = ?2")
    Integer updateTaskState(Long userId, String taskId, TaskState taskState);
}
