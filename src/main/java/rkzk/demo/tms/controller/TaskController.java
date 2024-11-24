package rkzk.demo.tms.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rkzk.demo.tms.model.Task;
import rkzk.demo.tms.service.TaskService;
import rkzk.demo.tms.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class TaskController {
    @Autowired
    private final TaskService taskService;
    @Autowired
    private final UserService userService;

    Logger logger = (Logger) LoggerFactory.getLogger(TaskController.class);

    @Operation(summary = "Create a new task")
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        try {
            Task createdTask = taskService.createTask(task);
            return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating task: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        Task task = taskService.getTask(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping("/owner/{username}")
    public ResponseEntity<List<Task>> getAuthorTasks(@PathVariable String username) {
        List<Task> task = taskService.getTasksByAuthor(userService.getByUsername(username));
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTask(@RequestBody Task updatedTask, @PathVariable Long id) {
        if (!updatedTask.getTaskId().equals(id)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        taskService.updateTask(updatedTask);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // More endpoints for filtering tasks by author, assignee, etc.
}
