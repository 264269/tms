package rkzk.demo.tms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rkzk.demo.tms.model.Comment;
import rkzk.demo.tms.model.Task;
import rkzk.demo.tms.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class TaskController {

    @Autowired
    private final TaskService taskService;

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        Task task = taskService.getByIdRequest(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

//    @GetMapping("/owner/{id}")
//    public ResponseEntity<List<Task>> getAuthorTasks(@PathVariable Long id) {
//        List<Task> task = taskService.getTasksByOwnerRequest(id);
//        return new ResponseEntity<>(task, HttpStatus.OK);
//    }

//    @GetMapping("/executor/{id}")
//    public ResponseEntity<List<Task>> getExecutorTasks(@PathVariable Long id) {
//        List<Task> task = taskService.getTasksByExecutorRequest(id);
//        return new ResponseEntity<>(task, HttpStatus.OK);
//    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Task> updateTask(@RequestBody TaskRequest updatedTask, @PathVariable Long id) {
//        Task task = taskService.updateTaskRequest(updatedTask, id);
//        return new ResponseEntity<>(task, HttpStatus.OK);
//    }
//
//    @PutMapping
//    public ResponseEntity<Task> createTask(@RequestBody TaskRequest updatedTask) {
//        Task task = taskService.createTaskRequest(updatedTask);
//        return new ResponseEntity<>(task, HttpStatus.OK);
//    }

    @PutMapping
    public ResponseEntity<Task> createOrUpdateTask(
            @RequestBody TaskRequest updatedTask,
            @RequestParam(value = "id", required = false) Long id) {
        Task task;
        if (id == null) {
            task = taskService.createTaskRequest(updatedTask);
        } else {
            task = taskService.updateTaskRequest(updatedTask, id);
        }
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

//    @PutMapping
//    public ResponseEntity<List<Comment>> getComments() {
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    public record TaskRequest(
            String title,
            String description,
            Long priorityId,
            Long statusId,
            Long executorId) { }

    public record FilterRequest(
            Long owner,
            Long executor,
            Long status,
            Long priority
    ) { }
}
