package rkzk.demo.tms.auth;

import rkzk.demo.tms.entity.User;

import java.util.List;

public interface AuthService {
    User save(User user);
    Iterable<User> get();
}