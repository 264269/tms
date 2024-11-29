package rkzk.demo.tms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rkzk.demo.tms.model.persistent.Priority;

@Repository
public interface PriorityRepository extends JpaRepository<Priority, Long> {
}