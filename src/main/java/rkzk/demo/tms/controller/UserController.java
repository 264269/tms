package rkzk.demo.tms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rkzk.demo.tms.model.CustomUser;
import rkzk.demo.tms.model.Task;
import rkzk.demo.tms.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<CustomUser> getUser(@PathVariable Long id) {
        CustomUser user = userService.getById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<Task>> getTasks(@PathVariable Long id) {
        CustomUser user = userService.getById(id);
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(user.getOwnedTasks());
        allTasks.addAll(user.getAssignedTasks());
        return new ResponseEntity<>(allTasks, HttpStatus.OK);
    }

    @GetMapping("/{id}/tasks/owner")
    public ResponseEntity<List<Task>> getTasksAsOwner(@PathVariable Long id) {
        CustomUser user = userService.getById(id);
        List<Task> allTasks = new ArrayList<>(user.getOwnedTasks());
        return new ResponseEntity<>(allTasks, HttpStatus.OK);
    }

    @GetMapping("/{id}/tasks/executor")
    public ResponseEntity<List<Task>> getTasksAsExecutor(@PathVariable Long id) {
        CustomUser user = userService.getById(id);
        List<Task> allTasks = new ArrayList<>(user.getAssignedTasks());
        return new ResponseEntity<>(allTasks, HttpStatus.OK);
    }
}
