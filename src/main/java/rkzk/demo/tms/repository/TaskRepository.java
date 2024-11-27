package rkzk.demo.tms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import rkzk.demo.tms.model.CustomUser;
import rkzk.demo.tms.model.Task;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    List<Task> findByOwner(CustomUser owner);
    List<Task> findByOwnerId(Long ownerId);
    List<Task> findByExecutor(CustomUser executor);
    List<Task> findByExecutorId(Long executorId);
}