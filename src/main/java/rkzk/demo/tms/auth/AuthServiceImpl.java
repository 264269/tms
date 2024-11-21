package rkzk.demo.tms.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rkzk.demo.tms.entity.User;
import rkzk.demo.tms.repository.UserRepository;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Iterable<User> get() {
        return userRepository.findAll();
    }
}
