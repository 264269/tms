package rkzk.demo.tms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rkzk.demo.tms.model.CustomUser;
import rkzk.demo.tms.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public CustomUser getUser(@RequestParam Long id) {
        return userService.getUser(id);
    }
}
