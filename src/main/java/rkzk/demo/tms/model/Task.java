package rkzk.demo.tms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import rkzk.demo.tms.controller.TaskController;
import rkzk.demo.tms.model.persistent.Priority;
import rkzk.demo.tms.model.persistent.TaskStatus;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "tasks")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Task {
    @Id
    @Column(name = "task_id", nullable = false)
    @GeneratedValue
    private Long taskId;

    private String title;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "priority")
    private Priority priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status")
    private TaskStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner")
    @JsonIgnore
    private CustomUser owner;
    @Column(name = "owner", insertable = false, updatable = false)
    private Long ownerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "executor")
    @JsonIgnore
    private CustomUser executor;
    @Column(name = "executor", insertable = false, updatable = false)
    private Long executorId;

    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>();

    public void addComment(Comment reply) {
        comments.add(reply);
        reply.setTask(this);
    }

    public void setOwner(CustomUser owner) {
        this.owner = owner;
        ownerId = owner.getUserId();
    }

    public void updateOwner() {
        ownerId = owner.getUserId();
    }

    public void setExecutor(CustomUser executor) {
        this.executor = executor;
        executorId = executor.getUserId();
    }

    public void updateExecutor() {
        executorId = executor.getUserId();
    }

    public void update() {
        updateExecutor();
        updateOwner();
    }
}
