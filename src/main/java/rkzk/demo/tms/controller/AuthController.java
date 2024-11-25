package rkzk.demo.tms.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rkzk.demo.tms.model.CustomUser;
import rkzk.demo.tms.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/sign-up")
    public CustomUser signUp(@RequestBody SignUpRequest request) {
        return userService.create(new UserService.UserCredentials(
                request.username,
                request.email,
                request.password));
    }

    @PostMapping("/sign-in")
    public CustomUser signIn(@RequestBody SignInRequest request) {
        return userService.getByUsername(request.username);
    }

    public record SignInRequest(
            @NotNull
            String username,
            @NotNull
            String password) { }

    public record SignUpRequest(
            @NotNull
            String username,
            @Email
            @NotNull
            String email,
            @NotNull
            String password) { }

    public record JwtAuthenticationResponse (String token) { }
}