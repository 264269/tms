package rkzk.demo.tms.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    @GeneratedValue
    private Long commentId;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    @JsonIgnore
    private Task task;
    @Column(name = "task_id", insertable = false, updatable = false)
    private Long taskId;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    @JsonIgnore
    private Comment parentComment;
    @Column(name = "parent_comment_id", insertable = false, updatable = false)
    private Long parentCommentId;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> replies = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private CustomUser owner;
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long ownerId;

    public void addReply(Comment reply) {
        replies.add(reply);
        reply.setParentComment(this);
    }

    public void setTask(Task task) {
        this.task = task;
        this.taskId = task.getTaskId();
    }
    public void updateTask() {
        taskId = task.getTaskId();
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
        this.parentCommentId = parentComment.parentCommentId;
    }
    public void updateParentComment() {
        parentCommentId = parentComment.parentCommentId;
    }

    public void setOwner(CustomUser owner) {
        this.owner = owner;
        this.ownerId = owner.getUserId();
    }
    public void updateOwner() {
        ownerId = owner.getUserId();
    }

    public void update() {
        updateOwner();
        updateTask();
        updateParentComment();
    }
}