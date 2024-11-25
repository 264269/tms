//package rkzk.demo.tms.service;
//
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotNull;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import rkzk.demo.tms.model.CustomUser;
//
//@Service
//public class AuthService {
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private JwtService jwtService;
//
////    public JwtAuthenticationResponse signUp(SignUpRequest request) {
////        CustomUser user = userService.create(request);
////
////        return new JwtAuthenticationResponse(jwtService.generateToken(user));
////    }
////
////    public JwtAuthenticationResponse signIn(SignInRequest request) {
////
////        CustomUser user = userService.getByUsername(request.username());
////
////        if (!user.getPassword().equals(request.password()))
////            throw new RuntimeException("Wrong password");
////
////        return new JwtAuthenticationResponse(jwtService.generateToken(user));
////    }
//}
