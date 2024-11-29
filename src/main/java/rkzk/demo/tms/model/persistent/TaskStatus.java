package rkzk.demo.tms.model.persistent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Arrays;
import java.util.Map;
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
    public enum TaskStatusEnum {
        CREATED(1L, "created"),
        EXECUTING(2L, "executing"),
        FINISHED(3L, "finished");

        private final Long statusId;
        private final String description;

        private TaskStatus taskStatus;

        TaskStatusEnum(Long statusId, String description) {
            this.statusId = statusId;
            this.description = description;
        }

        public TaskStatus getTaskStatus() {
            if (taskStatus == null) {
                throw new IllegalStateException(
                        "TaskStatusEnum not initialized. Ensure TaskStatusEnum is initialized during application startup.");
            }
            return taskStatus;
        }

        public static TaskStatus getById(Long id) {
            return Arrays.stream(values())
                    .filter(s -> Objects.equals(s.statusId, id))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No taskStatus with such ID"))
                    .getTaskStatus();
        }

        public static TaskStatus getByDescription(String description) {
            return Arrays.stream(values())
                    .filter(s -> Objects.equals(s.description, description))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No task status with such description"))
                    .getTaskStatus();
        }

        public static void initialize(Map<Long, TaskStatus> statuses) {
            for (TaskStatus.TaskStatusEnum statusEnum : values()) {
                TaskStatus taskStatus = statuses.get(statusEnum.statusId);
                if (taskStatus == null || !taskStatus.getDescription().equals(statusEnum.description)) {
                    throw new IllegalStateException(
                            String.format("TaskStatusEnum mismatch for %s: DB contains invalid or missing value.",
                                    statusEnum.name()));
                }
                statusEnum.taskStatus = taskStatus;
            }
        }
    }
}

