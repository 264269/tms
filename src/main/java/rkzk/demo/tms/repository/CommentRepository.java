package rkzk.demo.tms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rkzk.demo.tms.model.Comment;
import rkzk.demo.tms.model.Task;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTask(Task task);
    List<Comment> findByTaskId(Long taskId);
    Page<Comment> findByTask(Task task, PageRequest pageRequest);
    Page<Comment> findByTaskId(Long taskId, PageRequest pageRequest);
}
