package rkzk.demo.tms.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rkzk.demo.tms.model.CustomUser;
import rkzk.demo.tms.service.AuthService;
import rkzk.demo.tms.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody SignUpRequest request) {
        JwtAuthenticationResponse jwt = new JwtAuthenticationResponse(authService.signUp(new UserService.UserCredentials(
                request.username,
                request.email,
                request.password)));
        return jwt;
    }

    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody SignInRequest request) {
        JwtAuthenticationResponse jwt
                = new JwtAuthenticationResponse(
                        authService.signIn(new UserService.UserCredentials(
                                request.username,
                                null,
                                request.password)));
        return jwt;
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