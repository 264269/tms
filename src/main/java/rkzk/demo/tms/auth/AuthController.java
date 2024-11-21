package rkzk.demo.tms.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rkzk.demo.tms.entity.User;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        return authService.save(user);
    }

    @GetMapping
    public Iterable<User> get() {
        return authService.get();
    }
}
