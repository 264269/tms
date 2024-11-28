package rkzk.demo.tms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rkzk.demo.tms.model.Comment;
import rkzk.demo.tms.service.CommentService;
import rkzk.demo.tms.service.TaskService;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CommentController {
    @Autowired
    private final CommentService commentService;
    @Autowired
    private final TaskService taskService;

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getComment(@PathVariable Long id) {
        Comment comment = commentService.getByIdRequest(id);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    public record CommentRequest(
            String content,
            Long parentCommentId
    ) { }
}
