package rkzk.demo.tms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rkzk.demo.tms.data.SignInRequest;
import rkzk.demo.tms.data.SignUpRequest;
import rkzk.demo.tms.model.CustomUser;
import rkzk.demo.tms.model.persistent.Role;
//import rkzk.demo.tms.model.Task;
import rkzk.demo.tms.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public CustomUser create(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.email()))
            throw new RuntimeException("User with this email already exists");

        if (userRepository.existsByUsername(signUpRequest.username()))
            throw new RuntimeException("User with this username already exists");

        String encryptedPassword = passwordEncoder.encode(signUpRequest.password());

        CustomUser customUser = CustomUser.builder()
                .username(signUpRequest.username())
                .email(signUpRequest.email())
                .password(encryptedPassword)
                .roles(List.of(Role.RoleEnum.USER.getRole()))
                .build();

        return save(customUser);
    }

    private CustomUser save(CustomUser customUser) {
        return userRepository.save(customUser);
    }

    public boolean isValidUser(SignInRequest signInRequest) {
        return true;
    }

    public CustomUser getByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(
//                        () -> new UsernameNotFoundException("Пользователь не найден")
                        () -> new RuntimeException("User with username " + email + " not found"));
    }

    public CustomUser getUser(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Task not found"));
    }

    public CustomUser getByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(
//                        () -> new UsernameNotFoundException("Пользователь не найден")
                        () -> new RuntimeException("User with username " + username + " not found"));
    }

//    public void setAdmin(User user) {
//        user.setRole(RoleEnum.ADMIN);
//        save(user);
//    }
}
