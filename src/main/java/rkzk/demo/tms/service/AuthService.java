//package rkzk.demo.tms.service;
//
////import org.springframework.security.authentication.AuthenticationManager;
////import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
////import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import rkzk.demo.tms.data.JwtAuthenticationResponse;
//import rkzk.demo.tms.data.SignInRequest;
//import rkzk.demo.tms.data.SignUpRequest;
//import rkzk.demo.tms.model.User;
//
//@Service
//public class AuthService {
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private JwtService jwtService;
//
//    /**
//     * Регистрация пользователя
//     *
//     * @param request данные пользователя
//     * @return токен
//     */
//    public JwtAuthenticationResponse signUp(SignUpRequest request) {
//        User user = userService.create(request);
//
//        userService.setAdmin(user);
//
//        return new JwtAuthenticationResponse(jwtService.generateToken(user));
//    }
//
//    /**
//     * Аутентификация пользователя
//     *
//     * @param request данные пользователя
//     * @return токен
//     */
//    public JwtAuthenticationResponse signIn(SignInRequest request) {
////        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
////                request.email(),
////                request.password()
////        ));
//
////        var user = userService
////                .userDetailsService()
////                .loadUserByUsername(request.username());
//
//        User user = userService.getByUsername(request.username());
//
//        if (!user.getPassword().equals(request.password()))
//            throw new RuntimeException("Wrong password");
//
//        return new JwtAuthenticationResponse(jwtService.generateToken(user));
//    }
//}
