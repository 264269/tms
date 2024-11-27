package rkzk.demo.tms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import rkzk.demo.tms.model.CustomUser;
import rkzk.demo.tms.model.Task;
import rkzk.demo.tms.model.persistent.Role;
import rkzk.demo.tms.repository.CustomUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private CustomUserRepository customUserRepository;

    public CustomUser create(UserCredentials userCredentials) {
        if (customUserRepository.existsByEmail(userCredentials.email()))
            throw new RuntimeException("User with this email already exists");

        if (customUserRepository.existsByUsername(userCredentials.username()))
            throw new RuntimeException("User with this username already exists");

        List<Role> roles = new ArrayList<>();
        roles.add(Role.RoleEnum.USER.getRole());

        CustomUser customUser = CustomUser.builder()
                .username(userCredentials.username())
                .email(userCredentials.email())
                .password(userCredentials.password())
                .roles(roles)
                .enabled(true)
                .build();

        return save(customUser);
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    private CustomUser save(CustomUser customUser) {
        return customUserRepository.save(customUser);
    }

    public CustomUser getById(Long id) {
        return customUserRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User with id " + id + " not found"));
    }
    public CustomUser getByIdRequest(Long id) {
        if (!checkAccessToUser(id)) {
            throw new AccessDeniedException("You're not owning this account");
        }
        return getById(id);
    }

    public CustomUser getByUsername(String username) {
        return customUserRepository
                .findByUsername(username)
                .orElseThrow(
                        () -> new RuntimeException("User with username " + username + " not found"));
    }

    public boolean checkAccessToUser(Long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUser authUser = (CustomUser) auth.getPrincipal();

        boolean sameId = Objects.equals(authUser.getUserId(), userId);

        return sameId || authUser.isAdmin();
    }

    public List<Task> getTasks(Long id) {
        CustomUser user = getById(id);
        List<Task> tasks = new ArrayList<>();
        tasks.addAll(user.getOwnedTasks());
        tasks.addAll(user.getAssignedTasks());
        return tasks;
    }
    public List<Task> getTasksRequest(Long id) {
        if (!checkAccessToUser(id)) {
            throw new AccessDeniedException("You're not owning this account");
        }
        return getTasks(id);
    }

    public List<Task> getTasksAsOwner(Long id) {
        CustomUser user = getById(id);
        return user.getOwnedTasks();
    }
    public List<Task> getTasksAsOwnerRequest(Long id) {
        if (!checkAccessToUser(id)) {
            throw new AccessDeniedException("You're not owning this account");
        }
        return getTasksAsOwner(id);
    }

    public List<Task> getTasksAsExecutor(Long id) {
        CustomUser user = getById(id);
        return user.getAssignedTasks();
    }
    public List<Task> getTasksAsExecutorRequest(Long id) {
        if (!checkAccessToUser(id)) {
            throw new AccessDeniedException("You're not owning this account");
        }
        return getTasksAsExecutor(id);
    }

//    @Schema(description = "Данные пользователя")
    public record UserCredentials (
//            @Schema(description = "Имя пользователя", example = "Jon")
//            @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
//            @NotBlank(message = "Имя пользователя не может быть пустыми")
            String username,
//            @Schema(description = "Адрес электронной почты", example = "jondoe@gmail.com")
//            @Size(min = 5, max = 255, message = "Адрес электронной почты должен содержать от 5 до 255 символов")
//            @NotBlank(message = "Адрес электронной почты не может быть пустыми")
//            @Email(message = "Email адрес должен быть в формате user@example.com")
            String email,
//            @Schema(description = "Пароль", example = "my_1secret1_password")
//            @Size(max = 255, message = "Длина пароля должна быть не более 255 символов")
            String password) {
    }
}
