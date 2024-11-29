package rkzk.demo.tms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rkzk.demo.tms.controller.CommentController;
import rkzk.demo.tms.model.Comment;
import rkzk.demo.tms.model.CustomUser;
import rkzk.demo.tms.model.Task;
import rkzk.demo.tms.repository.CommentRepository;

import java.util.Objects;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;

    public Page<Comment> getByTask(Long taskId, PageRequest pageRequest) {
        return commentRepository.findByTaskId(taskId, pageRequest);
    }
    public Page<Comment> getByTaskRequest(Long taskId, PageRequest pageRequest) {
        Task task = taskService.getByIdRequest(taskId);
        boolean isOwner = taskService.checkAccessAsOwner(task);
        boolean isExecutor = taskService.checkAccessAsExecutor(task);
        if (!isOwner && !isExecutor) {
            throw new AccessDeniedException("You're neither owning nor executing this task");
        }
        return getByTask(taskId, pageRequest);
    }

    public Comment getById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
    }
    public Comment getByIdRequest(Long id) {
        Comment comment = getById(id);
        if (!checkAccessAsOwner(comment)) {
            throw new AccessDeniedException("You're not owning this comment");
        }
        return comment;
    }

    public boolean checkAccessAsOwner(Comment comment) {
        CustomUser authUser = userService.getUserFromContext();

        boolean owner = Objects.equals(comment.getOwnerId(), authUser.getUserId());

        return owner || authUser.isAdmin();
    }

    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }
    public Comment addCommentRequest(Long id, CommentController.CommentRequest commentRequest) {
        CustomUser user = userService.getUserFromContext();

        Task task = taskService.getByIdRequest(id);

        Comment comment =
                Comment.builder()
                        .content(commentRequest.content())
                        .task(task)
                        .owner(user)
                .build();

        comment.update();

        return addComment(comment);
    }
}
