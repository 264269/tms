package rkzk.demo.tms.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.*;
import rkzk.demo.tms.controller.TaskController;
import rkzk.demo.tms.model.CustomUser;
import rkzk.demo.tms.model.Task;
import rkzk.demo.tms.model.persistent.Priority;
import rkzk.demo.tms.model.persistent.TaskStatus;
import rkzk.demo.tms.repository.TaskRepository;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TaskServiceIntegrationTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private CustomUser testOwner;
    private CustomUser testExecutor;

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

        // Очистка контекста безопасности
        SecurityContextHolder.clearContext();
    }

    @Test
    @WithUserDetails(value = "owner", userDetailsServiceBeanName = "userDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void createTaskRequest_shouldCreateNewTask() {
        TaskController.TaskRequest taskRequest = new TaskController.TaskRequest(
                "Test Task",
                "Task Description",
                Priority.PriorityEnum.HIGH.getPriority().getPriorityId(),
                TaskStatus.TaskStatusEnum.CREATED.getTaskStatus().getStatusId(),
                testExecutor.getUserId()
        );

        Task task = taskService.createTaskRequest(taskRequest);

        assertNotNull(task.getTaskId());
        assertEquals("Test Task", task.getTitle());
        assertEquals(testOwner.getUserId(), task.getOwnerId());
        assertEquals(testExecutor.getUserId(), task.getExecutorId());
    }

    @Test
    @WithUserDetails(value = "executor",  userDetailsServiceBeanName = "userDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void getByIdRequest_shouldReturnTaskForExecutor() {
        // Создаем задачу с помощью репозитория
        Task task = Task.builder()
                .title("Executor Task")
                .description("Executor Task Description")
                .priority(Priority.PriorityEnum.HIGH.getPriority())
                .status(TaskStatus.TaskStatusEnum.CREATED.getTaskStatus())
                .owner(testOwner)
                .executor(testExecutor)
                .build();

        task = taskRepository.save(task);

        Task fetchedTask = taskService.getByIdRequest(task.getTaskId());

        assertEquals(task.getTaskId(), fetchedTask.getTaskId());
        assertEquals("Executor Task", fetchedTask.getTitle());
    }

    @Test
    @WithUserDetails(value = "owner",  userDetailsServiceBeanName = "userDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void updateTaskRequest_shouldUpdateTaskForOwner() {
        // Создаем задачу с помощью репозитория
        Task task = Task.builder()
                .title("Old Title")
                .description("Old Description")
                .priority(Priority.PriorityEnum.LOW.getPriority())
                .status(TaskStatus.TaskStatusEnum.CREATED.getTaskStatus())
                .owner(testOwner)
                .executor(testExecutor)
                .build();

        task = taskRepository.save(task);

        // Обновляем задачу
        TaskController.TaskRequest updateRequest = new TaskController.TaskRequest(
                "Updated Title",
                "Updated Description",
                Priority.PriorityEnum.HIGH.getPriority().getPriorityId(),
                TaskStatus.TaskStatusEnum.EXECUTING.getTaskStatus().getStatusId(),
                null
        );

        Task updatedTask = taskService.updateTaskRequest(updateRequest, task.getTaskId());

        assertEquals("Updated Title", updatedTask.getTitle());
        assertEquals("Updated Description", updatedTask.getDescription());
        assertEquals(Priority.PriorityEnum.HIGH.getPriority(), updatedTask.getPriority());
        assertEquals(TaskStatus.TaskStatusEnum.EXECUTING.getTaskStatus(), updatedTask.getStatus());
    }

    @Test
    @WithUserDetails(value = "executor",  userDetailsServiceBeanName = "userDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deleteTaskRequest_shouldThrowAccessDeniedForNonOwner() {
        // Создаем задачу с помощью репозитория
        Task task = Task.builder()
                .title("Delete Test Task")
                .description("Task Description")
                .priority(Priority.PriorityEnum.LOW.getPriority())
                .status(TaskStatus.TaskStatusEnum.CREATED.getTaskStatus())
                .owner(testOwner)
                .executor(testExecutor)
                .build();

        task = taskRepository.save(task);

        Task finalTask = task;
        assertThrows(AccessDeniedException.class, () -> taskService.deleteTaskRequest(finalTask.getTaskId()));
    }

    @Test
    @WithUserDetails(value = "owner",  userDetailsServiceBeanName = "userDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void getTasksFilteredRequest_shouldReturnTasksForOwner() {
        // Создаем несколько задач
        Task task1 = Task.builder()
                .title("Task 1")
                .description("Description 1")
                .priority(Priority.PriorityEnum.HIGH.getPriority())
                .status(TaskStatus.TaskStatusEnum.CREATED.getTaskStatus())
                .owner(testOwner)
                .executor(testExecutor)
                .build();

        Task task2 = Task.builder()
                .title("Task 2")
                .description("Description 2")
                .priority(Priority.PriorityEnum.LOW.getPriority())
                .status(TaskStatus.TaskStatusEnum.CREATED.getTaskStatus())
                .owner(testOwner)
                .executor(testExecutor)
                .build();

        taskRepository.saveAll(List.of(task1, task2));

        // Создаем фильтр
        TaskController.FilterRequest filterRequest = new TaskController.FilterRequest(
                testOwner.getUserId(),
                null,
                TaskStatus.TaskStatusEnum.CREATED.getTaskStatus().getStatusId(),
                null
        );

        Page<Task> tasks = taskService.getTasksFilteredRequest(filterRequest, PageRequest.of(0, 10));

        assertEquals(2, tasks.getTotalElements());
    }
}
