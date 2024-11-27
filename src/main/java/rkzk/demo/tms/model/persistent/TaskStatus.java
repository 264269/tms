package rkzk.demo.tms.model.persistent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "statuses")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TaskStatus {
    @Id
    @Column(name = "status_id", nullable = false, unique = true)
    private Long statusId;

    @Column(nullable = false, unique = true, length = 20)
    private String description;

    @Getter
    @RequiredArgsConstructor
    public enum TaskStatusEnum {
        CREATED(new TaskStatus(1L, "created")),
        EXECUTING(new TaskStatus(2L, "executing")),
        FINISHED(new TaskStatus(3L, "finished"));

        private final TaskStatus taskStatus;

        public static TaskStatus getById(Long id) {
            for (TaskStatusEnum taskStatusEnum : values()) {
                if (Objects.equals(taskStatusEnum.taskStatus.statusId, id))
                    return taskStatusEnum.taskStatus;
            }
            throw new IllegalArgumentException("No taskStatus with such value");
        }

        public static TaskStatus getByDescription(String description) {
            for (TaskStatusEnum taskStatusEnum : values()) {
                if (Objects.equals(taskStatusEnum.taskStatus.description, description))
                    return taskStatusEnum.taskStatus;
            }
            throw new IllegalArgumentException("No taskStatus with such name");
        }
    }
}

