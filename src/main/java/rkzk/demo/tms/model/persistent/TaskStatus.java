package rkzk.demo.tms.model.persistent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

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
    }
}

