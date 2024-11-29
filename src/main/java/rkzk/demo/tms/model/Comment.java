package rkzk.demo.tms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "comments")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Comment {
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id")
    @JsonIgnore
    private Task task;
    @Column(name = "task_id", insertable = false, updatable = false)
    private Long taskId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private CustomUser owner;
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long ownerId;

    public void setTask(Task task) {
        this.task = task;
        this.taskId = task.getTaskId();
    }
    public void updateTask() {
        if (task == null) return;
        taskId = task.getTaskId();
    }

    public void setOwner(CustomUser owner) {
        this.owner = owner;
        this.ownerId = owner.getUserId();
    }
    public void updateOwner() {
        if (owner == null) return;
        ownerId = owner.getUserId();
    }

    public void update() {
        updateOwner();
        updateTask();
    }
}