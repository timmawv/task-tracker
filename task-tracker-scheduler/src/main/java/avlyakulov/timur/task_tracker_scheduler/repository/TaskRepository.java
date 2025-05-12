package avlyakulov.timur.task_tracker_scheduler.repository;

import avlyakulov.timur.task_tracker_scheduler.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

    @Query("select t.owner.email, t.id, t.title, t.finishedAt, t.taskState  from Task t left join User u on u.id = t.owner.id")
    List<Task> findTasksWithUserEmail();
}