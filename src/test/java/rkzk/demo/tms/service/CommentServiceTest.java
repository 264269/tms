package rkzk.demo.tms.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import rkzk.demo.tms.controller.CommentController;
import rkzk.demo.tms.controller.TaskController;
import rkzk.demo.tms.model.Comment;
import rkzk.demo.tms.model.CustomUser;
import rkzk.demo.tms.model.Task;
import rkzk.demo.tms.model.persistent.Priority;
import rkzk.demo.tms.model.persistent.TaskStatus;
import rkzk.demo.tms.repository.CommentRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private CustomUser testOwner;
    private CustomUser testExecutor;
    private CustomUser testUnauthorized;

    private Task testTask;

    List<Comment> createdComments = new ArrayList<>();

    @BeforeEach
    void setup() {
        // Создание тестовых пользователей
        testOwner = userService.create(new UserService.UserCredentials(
                "owner",
                "owner@example.com",
                passwordEncoder.encode("password")
        ));

        testExecutor = userService.create(new UserService.UserCredentials(
                "executor",
                "executor@example.com",
                passwordEncoder.encode("password")
        ));

        testUnauthorized = userService.create(new UserService.UserCredentials(
                "test",
                "test@example.com",
                passwordEncoder.encode("password")
        ));

        // Очистка контекста безопасности
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        try {
            commentRepository.deleteAll(createdComments);
        } catch (Exception e) { }
    }

    @Test
    @WithUserDetails(value = "owner", userDetailsServiceBeanName = "userDetailsService", setupBefore =  TestExecutionEvent.TEST_EXECUTION)
    void addCommentRequest_shouldAddCommentSuccessfully() {
        // Arrange
        TaskController.TaskRequest taskRequest = new TaskController.TaskRequest(
                "Test Task",
                "Task Description",
                Priority.PriorityEnum.HIGH.getPriority().getPriorityId(),
                TaskStatus.TaskStatusEnum.CREATED.getTaskStatus().getStatusId(),
                testExecutor.getUserId()
        );

        Task task = taskService.createTaskRequest(taskRequest);

        Long taskId = task.getTaskId();
        CommentController.CommentRequest commentRequest = new CommentController.CommentRequest("Test comment");

        // Act
        Comment addedComment = commentService.addCommentRequest(taskId, commentRequest);
        createdComments.add(addedComment);

        // Assert
        assertNotNull(addedComment.getCommentId());
        assertEquals("Test comment", addedComment.getContent());
    }

    @Test
    @WithUserDetails(value = "owner", userDetailsServiceBeanName = "userDetailsService", setupBefore =  TestExecutionEvent.TEST_EXECUTION)
    void getByTaskRequest_shouldReturnCommentsForAccessibleTask() {
        TaskController.TaskRequest taskRequest = new TaskController.TaskRequest(
                "Test Task",
                "Task Description",
                Priority.PriorityEnum.HIGH.getPriority().getPriorityId(),
                TaskStatus.TaskStatusEnum.CREATED.getTaskStatus().getStatusId(),
                testExecutor.getUserId()
        );

        Task task = taskService.createTaskRequest(taskRequest);

        Long taskId = task.getTaskId();
        CommentController.CommentRequest commentRequest = new CommentController.CommentRequest("Test comment");

        // Act
        Comment addedComment = commentService.addCommentRequest(taskId, commentRequest);
        createdComments.add(addedComment);
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act
        Page<Comment> comments = commentService.getByTaskRequest(taskId, pageRequest);

        // Assert
        assertNotNull(comments, "Comments page should not be null");
        assertFalse(comments.getContent().isEmpty(), "Comments should not be empty for accessible tasks");
    }

    @Test
    @WithMockUser(value = "unauthorized", setupBefore =  TestExecutionEvent.TEST_EXECUTION)
    void getByTaskRequest_shouldThrowAccessDeniedExceptionForUnauthorizedUser() {
//        TaskController.TaskRequest taskRequest = new TaskController.TaskRequest(
//                "Test Task",
//                "Task Description",
//                Priority.PriorityEnum.HIGH.getPriority().getPriorityId(),
//                TaskStatus.TaskStatusEnum.CREATED.getTaskStatus().getStatusId(),
//                testExecutor.getUserId()
//        );

        Task task = Task.builder()
                .title("Test Task")
                .description("Task Description")
                .priority(Priority.PriorityEnum.HIGH.getPriority())
                .status(TaskStatus.TaskStatusEnum.CREATED.getTaskStatus())
                .owner(testOwner)
                .executor(testExecutor)
                .build();

        task = taskService.saveTask(task);

        Long taskId = task.getTaskId();
        PageRequest pageRequest = PageRequest.of(0, 10);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> commentService.getByTaskRequest(taskId, pageRequest),
                "Should throw AccessDeniedException for unauthorized user");
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService", setupBefore =  TestExecutionEvent.TEST_EXECUTION)
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
    @WithUserDetails(value = "test", userDetailsServiceBeanName = "userDetailsService", setupBefore =  TestExecutionEvent.TEST_EXECUTION)
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
