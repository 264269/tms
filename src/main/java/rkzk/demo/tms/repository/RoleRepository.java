package rkzk.demo.tms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rkzk.demo.tms.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
