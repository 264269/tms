package rkzk.demo.tms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rkzk.demo.tms.model.Task;
import rkzk.demo.tms.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class TaskController {

    @Autowired
    private final TaskService taskService;

//    @PostMapping
//    public ResponseEntity<Task> createTask(@RequestBody Task task) {
//        try {
//            Task createdTask = taskService.createTask(task);
//            return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
//        } catch (Exception e) {
//            logger.error("Error creating task: {}", e.getMessage());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        Task task = taskService.getByIdRequest(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping("/owner/{id}")
    public ResponseEntity<List<Task>> getAuthorTasks(@PathVariable Long id) {
        List<Task> task = taskService.getTasksByOwnerRequest(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping("/executor/{id}")
    public ResponseEntity<List<Task>> getExecutorTasks(@PathVariable Long id) {
        List<Task> task = taskService.getTasksByExecutorRequest(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@RequestBody TaskRequest updatedTask, @PathVariable Long id) {
        Task task = taskService.updateTaskRequest(updatedTask, id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Task> createTask(@RequestBody TaskRequest updatedTask) {
        Task task = taskService.createTaskRequest(updatedTask);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTaskRequest(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
//        taskService.deleteTask(id);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

    public record TaskRequest(
            String title,
            String description,
            Long priorityId,
            Long statusId,
            Long executorId) { }
}
