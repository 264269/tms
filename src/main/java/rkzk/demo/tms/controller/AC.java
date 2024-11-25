//package rkzk.demo.tms.controller;
//
//import jakarta.validation.Valid;
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotNull;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//import rkzk.demo.tms.service.JwtService;
////import rkzk.demo.tms.data.SignInRequest;
////import rkzk.demo.tms.data.SignUpRequest;
////import rkzk.demo.tms.data.JwtAuthenticationResponse;
////import rkzk.demo.tms.service.AuthService;
//
//@RestController
//@RequestMapping("/auth")
//public class A {
////    @Autowired
////    private AuthService authService;
//
////    @PostMapping("/sign-up")
////    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request) {
////        return authService.signUp(request);
////    }
////
////    @PostMapping("/sign-in")
////    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
////        return authService.signIn(request);
////    }
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@Valid @RequestBody SignInRequest loginRequest) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        String jwt = JwtService.generateToken(authentication);
//        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
//    }
//
//        public record SignInRequest(
//            @NotNull
//            String username,
//            @NotNull
//            String password) {
//    }
//
//    public record SignUpRequest(
//            @NotNull
//            String username,
//            @Email
//            @NotNull
//            String email,
//            @NotNull
//            String password) {
//    }
//
//    public record JwtAuthenticationResponse (
//            String token) {
//    }
//}
