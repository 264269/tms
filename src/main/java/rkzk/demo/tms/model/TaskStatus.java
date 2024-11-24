package rkzk.demo.tms.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "task_statuses")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TaskStatus {
    @Id
    @Column(name = "status_id")
    private Long statusId;

    @Column(nullable = false, unique = true, length = 20)
    private String description;

    protected TaskStatus() {}

    private TaskStatus(Long statusId, String description) {
        this.statusId = statusId;
        this.description = description;
    }

    public TaskStatus(TaskStatusEnum taskStatusEnum) {
        this(taskStatusEnum.getId(), taskStatusEnum.getDescription());
    }

    /**
     * Запрет изменений (статус неизменяем)
     * @param statusId
     * @throws IllegalAccessException
     */
    public void setStatusId(Long statusId) throws IllegalAccessException {
        if (this.statusId != 0)
            throw new IllegalAccessException(); // to prevent changes, id never 0 in db
        this.statusId = statusId;
    }

    /**
     * Запрет изменений (статус неизменяем)
     * @param description
     * @throws IllegalAccessException
     */
    public void setDescription(String description) throws IllegalAccessException {
        if (this.description != null)
            throw new IllegalAccessException();
        this.description = description;
    }
}

