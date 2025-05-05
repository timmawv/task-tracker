package avlyakulov.timur.taskTrackerApi.entity;

import avlyakulov.timur.taskTrackerApi.util.TaskState;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    @Id
    private String id;

    @NotNull
    @Column(nullable = false, unique = true)
    private String title;

    private String description;

    @Column(nullable = false)
    private Boolean isCompleted;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private TaskState taskState;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime finishedAt;

    //todo добавить каскадность пользователю, если его удаляют то все его задачи тоже
    //проблема с каскадностью когда ее добавляю то нельзя завершить транзакцию
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User owner;
}