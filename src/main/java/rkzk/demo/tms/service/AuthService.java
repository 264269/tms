package rkzk.demo.tms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rkzk.demo.tms.model.CustomUser;

@Service
public class AuthService {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    public String signUp(UserService.UserCredentials request) {

        String encoded = passwordEncoder.encode(request.password());

        UserDetails user = userService.create(new UserService.UserCredentials(
                request.username(),
                request.email(),
                encoded
        ));

        return jwtService.generateToken(user);
    }

    public String signIn(UserService.UserCredentials request) {
        String jwt = "";

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.username(),
                    request.password()
            ));

            UserDetails user = userService.userDetailsService().loadUserByUsername(request.username());

            for (GrantedAuthority a : user.getAuthorities()) {
                System.out.println(a.getAuthority());
            }

            jwt = jwtService.generateToken(user);
        } catch (Exception e) {
            System.out.println(e);
        }

        return jwt;
    }
}
