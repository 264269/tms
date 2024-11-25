package rkzk.demo.tms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rkzk.demo.tms.model.CustomUser;
import rkzk.demo.tms.model.persistent.Role;
import rkzk.demo.tms.repository.CustomUserRepository;

import java.util.ArrayList;
import java.util.List;

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

    private CustomUser save(CustomUser customUser) {
        return customUserRepository.save(customUser);
    }

    public CustomUser getByEmail(String email) {
        return customUserRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException("User with username " + email + " not found"));
    }

    public CustomUser getById(Long id) {
        return customUserRepository
                .findById(id)
                .orElseThrow(
                        () -> new RuntimeException("User with id " + id + " not found"));
    }

    public CustomUser getByUsername(String username) {
        return customUserRepository
                .findByUsername(username)
                .orElseThrow(
                        () -> new RuntimeException("User with username " + username + " not found"));
    }

    public record UserCredentials (
            String username,
            String email,
            String password) {
    }
}
