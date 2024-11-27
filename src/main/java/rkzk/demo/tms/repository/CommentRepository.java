package rkzk.demo.tms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rkzk.demo.tms.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
