package rkzk.demo.tms.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rkzk.demo.tms.model.Comment;
import rkzk.demo.tms.model.Task;
import rkzk.demo.tms.service.CommentService;
import rkzk.demo.tms.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class TaskController {

    @Autowired
    private final TaskService taskService;
    @Autowired
    private final CommentService commentService;

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(
            @PathVariable Long id,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "2") Integer size) {
//        get task
        Task task = taskService.getByIdRequest(id);
//        get task comments
        List<Comment> comments = commentService.getByTaskRequest(id, PageRequest.of(page, size)).getContent();
        TaskResponse taskResponse = new TaskResponse(task, comments);
        return new ResponseEntity<>(taskResponse, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Task> createTask(@RequestBody TaskRequest taskRequest) {
        Task task = taskService.createTaskRequest(taskRequest);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @RequestBody TaskRequest taskRequest,
            @PathVariable Long id) {
        Task task = taskService.updateTaskRequest(taskRequest, id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTaskRequest(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getTasks(
            @RequestParam(value = "owner", required = false) Long ownerId,
            @RequestParam(value = "executor", required = false) Long executorId,
            @RequestParam(value = "status", required = false) Long statusId,
            @RequestParam(value = "priority", required = false) Long priorityId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "2") Integer size) {
        Page<Task> tasks = taskService.getTasksFilteredRequest(
                new FilterRequest(
                        ownerId,
                        executorId,
                        statusId,
                        priorityId),
                PageRequest.of(page, size));
        return new ResponseEntity<>(tasks.getContent(), HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Comment> addComment(
            @RequestBody CommentController.CommentRequest commentRequest,
            @PathVariable Long id) {
        Comment comment = commentService.addCommentRequest(id, commentRequest);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

//    @PutMapping
//    public ResponseEntity<List<Comment>> getComments() {
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    public record TaskRequest(
            String title,
            String description,
            Long priority,
            Long status,
            Long executor) { }

    @Schema(description = "Параметры запроса")
    public record FilterRequest(
            Long owner,
            Long executor,
            Long status,
            Long priority
    ) { }

    @Schema(description = "Ответ с информацией о задаче.")
    public record TaskResponse(
            @Schema(description = "Задача")
            Task task,
            @Schema(description = "Комментарии")
            List<Comment> comments
    ) { }
}
