package rkzk.demo.tms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rkzk.demo.tms.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
//    Iterable<User> findAll();
}
