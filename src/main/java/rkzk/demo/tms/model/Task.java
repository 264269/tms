package rkzk.demo.tms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    private String title;
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "priority")
    private Priority priority;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status")
    private TaskStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner")
    @JsonIgnore
    private CustomUser owner;
    @Column(name = "owner", insertable = false, updatable = false)
    private Long ownerId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "executor")
    @JsonIgnore
    private CustomUser executor;
    @Column(name = "executor", insertable = false, updatable = false)
    private Long executorId;

    @OneToMany(mappedBy = "task", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
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

    @PreRemove
    public void preRemove() {
        dismissOwner();
        dismissExecutor();
        dismissAllComments();
    }

    public void dismissOwner() {
        this.owner.dismissOwned(this);
        this.owner = null;
    }

    public void dismissExecutor() {
        this.executor.dismissExecuting(this);
        this.executor = null;
    }

    public void dismissComment(Comment comment) {
        this.comments.remove(comment);
    }

    public void dismissAllComments() {
        if (this.comments == null) return;
        List<Comment> copyComments = new ArrayList<>(this.comments);
        for (Comment comment : copyComments) {
            this.comments.remove(comment);
        }
        this.comments.clear();
    }
}
