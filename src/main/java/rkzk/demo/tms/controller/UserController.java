package rkzk.demo.tms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rkzk.demo.tms.model.CustomUser;
import rkzk.demo.tms.model.Task;
import rkzk.demo.tms.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<CustomUser> getUser(@PathVariable Long userId) {
        CustomUser user = userService.getByIdRequest(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

//    @GetMapping("/{id}/tasks")
//    public ResponseEntity<List<Task>> getTasks(@PathVariable Long id) {
//        List<Task> tasks = userService.getTasksRequest(id);
//        return new ResponseEntity<>(tasks, HttpStatus.OK);
//    }
//
//    @GetMapping("/{id}/tasks/owner")
//    public ResponseEntity<List<Task>> getTasksAsOwner(@PathVariable Long id) {
//        List<Task> owned = userService.getTasksAsOwnerRequest(id);
//        return new ResponseEntity<>(owned, HttpStatus.OK);
//    }
//
//    @GetMapping("/{id}/tasks/executor")
//    public ResponseEntity<List<Task>> getTasksAsExecutor(@PathVariable Long id) {
//        List<Task> executing = userService.getTasksAsExecutorRequest(id);
//        return new ResponseEntity<>(executing, HttpStatus.OK);
//    }
}
