package rkzk.demo.tms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rkzk.demo.tms.data.SignInRequest;
import rkzk.demo.tms.data.SignUpRequest;
import rkzk.demo.tms.model.Role;
import rkzk.demo.tms.model.RoleEnum;
import rkzk.demo.tms.model.User;
import rkzk.demo.tms.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

//    public User create(UserCredentials userCredentials) {
//        if (userRepository.existsByEmail(userCredentials.email()))
//            throw new RuntimeException("User with this email already exists");
//
//        if (userRepository.existsByUsername(userCredentials.username()))
//            throw new RuntimeException("User with this username already exists");
//
//        User user = new User(userCredentials);
//
//        return save(user);
//    }

    /**
     * Создание и сохранение пользователя
     * @param signUpRequest - логин, почта, пароль
     * @return
     */
    public User create(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.email()))
            throw new RuntimeException("User with this email already exists");

        if (userRepository.existsByUsername(signUpRequest.username()))
            throw new RuntimeException("User with this username already exists");

        User user = User
                .builder()
                .username(signUpRequest.username())
                .email(signUpRequest.email())
                .password(signUpRequest.password())
                .role(new Role(RoleEnum.USER))
                .build();

        return save(user);
    }

    private User save(User user) {
        return userRepository.save(user);
    }

    public boolean isValidUser(SignInRequest signInRequest) {
        User user = userRepository.findByUsername(signInRequest.username()).get();
        return user.checkPassword(signInRequest.password());
    }

    public User getByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(
//                        () -> new UsernameNotFoundException("Пользователь не найден")
                        () -> new RuntimeException("User with username " + email + " not found")
                );
    }

    public User getByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(
//                        () -> new UsernameNotFoundException("Пользователь не найден")
                        () -> new RuntimeException("User with username " + username + " not found")
                );
    }

    public void setAdmin(User user) {
        user.setRole(RoleEnum.ADMIN);
        save(user);
    }
}
