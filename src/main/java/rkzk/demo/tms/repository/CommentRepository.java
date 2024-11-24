package rkzk.demo.tms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rkzk.demo.tms.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
