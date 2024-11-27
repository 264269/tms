package rkzk.demo.tms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rkzk.demo.tms.model.CustomUser;

import java.util.Optional;

@Repository
public interface CustomUserRepository extends JpaRepository<CustomUser, Long> {
    boolean existsByUsername(String username);
    Optional<CustomUser> findByUsername(String username);
    boolean existsByEmail(String email);
    Optional<CustomUser> findByEmail(String email);
}
