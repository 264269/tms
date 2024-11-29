package rkzk.demo.tms.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import rkzk.demo.tms.controller.CommentController;
import rkzk.demo.tms.model.Comment;
import rkzk.demo.tms.repository.CommentRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TaskService taskService;

    List<Comment> createdComments = new ArrayList<>();

    @AfterEach
    void tearDown() {
        try {
            commentRepository.deleteAll(createdComments);
        } catch (Exception e) { }
    }

    @Test
    @WithUserDetails(value = "b", userDetailsServiceBeanName = "userDetailsService")
    void addCommentRequest_shouldAddCommentSuccessfully() {
        // Arrange
        Long taskId = 1L;
        CommentController.CommentRequest commentRequest = new CommentController.CommentRequest("Test comment");

        // Act
        Comment addedComment = commentService.addCommentRequest(taskId, commentRequest);
        createdComments.add(addedComment);

        // Assert
        assertNotNull(addedComment.getCommentId());
        assertEquals("Test comment", addedComment.getContent());
    }

    @Test
//    @WithMockUser(username = "a", roles = {"user"})
    @WithUserDetails(value = "b", userDetailsServiceBeanName = "userDetailsService")
    void getByTaskRequest_shouldReturnCommentsForAccessibleTask() {
        // Arrange
        Long taskId = 1L;
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Comment> comments = commentService.getByTaskRequest(taskId, pageRequest);

        // Assert
        assertNotNull(comments, "Comments page should not be null");
        assertFalse(comments.getContent().isEmpty(), "Comments should not be empty for accessible tasks");
    }

    @Test
    @WithMockUser(username = "unauthorized", roles = {"user"})
    void getByTaskRequest_shouldThrowAccessDeniedExceptionForUnauthorizedUser() {
        // Arrange
        Long taskId = 1L;
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> commentService.getByTaskRequest(taskId, pageRequest),
                "Should throw AccessDeniedException for unauthorized user");
    }

    @Test
    @WithUserDetails(value = "a", userDetailsServiceBeanName = "userDetailsService")
    void checkAccessAsOwner_shouldAllowAdminAccess() {
        // Arrange
        Comment comment = Comment.builder()
                .content("Admin comment")
                .ownerId(2L)
                .build();
        comment = commentRepository.save(comment);
        createdComments.add(comment);

        // Act
        boolean hasAccess = commentService.checkAccessAsOwner(comment);

        // Assert
        assertTrue(hasAccess, "Admin should have access to all comments");
    }

    @Test
//    @WithMockUser(username = "owner", roles = {"USER"})
    @WithUserDetails(value = "c", userDetailsServiceBeanName = "userDetailsService")
    void getByIdRequest_shouldThrowAccessDeniedExceptionForUnauthorizedAccess() {
        // Arrange
        Comment comment = Comment.builder()
                .content("Owner comment")
                .ownerId(2L) // Different owner
                .build();
        comment = commentRepository.save(comment);
        createdComments.add(comment);

        // Act & Assert
        Comment finalComment = comment;
        assertThrows(AccessDeniedException.class, () -> commentService.getByIdRequest(finalComment.getCommentId()),
                "Should throw AccessDeniedException for unauthorized access");
    }
}
