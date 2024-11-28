package rkzk.demo.tms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rkzk.demo.tms.controller.CommentController;
import rkzk.demo.tms.controller.TaskController;
import rkzk.demo.tms.model.Comment;
import rkzk.demo.tms.model.CustomUser;
import rkzk.demo.tms.model.Task;
import rkzk.demo.tms.model.persistent.Priority;
import rkzk.demo.tms.model.persistent.TaskStatus;
import rkzk.demo.tms.repository.CommentRepository;
import rkzk.demo.tms.repository.TaskRepository;
import rkzk.demo.tms.repository.TaskSpecifications;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentRepository commentRepository;

    public Task getById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
    }
    public Task getByIdRequest(Long id) {
        Task task = getById(id);
        if (!checkAccessAsOwner(task) && !checkAccessAsExecutor(task)) {
            throw new AccessDeniedException("You're neither owning nor executing this task");
        }
        return task;
    }

    public List<Task> getTasksByOwner(Long ownerId) {
        return taskRepository.findByOwnerId(ownerId);
    }
    public List<Task> getTasksByOwnerRequest(Long ownerId) {
        if (!userService.checkAccessToUser(ownerId)) {
            throw new AccessDeniedException("You're not owning this account");
        }
        return getTasksByOwner(ownerId);
    }

    public List<Task> getTasksByExecutor(Long executorId) {
        return taskRepository.findByExecutorId(executorId);
    }
    public List<Task> getTasksByExecutorRequest(Long executorId) {
        if (!userService.checkAccessToUser(executorId)) {
            throw new AccessDeniedException("You're not owning this account");
        }
        return getTasksByExecutor(executorId);
    }

    public Task saveTask(Task task) {
        task.update();
        return taskRepository.save(task);
    }
//    TODO: add message what changed
    public Task updateTaskRequest(TaskController.TaskRequest taskRequest, Long taskId) {
        Task task = getByIdRequest(taskId);

        boolean isOwner = checkAccessAsOwner(task);

        if (isOwner) {
            //set title
            if (!Objects.isNull(taskRequest.title())) {
                task.setTitle(taskRequest.title());
            }

            //set description
            if (!Objects.isNull(taskRequest.description())) {
                task.setDescription(taskRequest.description());
            }

            //set priority
            try {
                task.setPriority(
                        Priority.PriorityEnum.getById(
                                taskRequest.priority()));
            } catch (Exception e) { }

            //set executor
            try {
                task.setExecutor(
                        userService.getById(
                                taskRequest.executor()));
            } catch (Exception e) { }
        }

        //set status
        try {
            task.setStatus(
                    TaskStatus.TaskStatusEnum.getById(
                            taskRequest.status()));
        } catch (Exception e) { }


        return saveTask(task);
    }
    public Task createTaskRequest(TaskController.TaskRequest taskRequest) {
        Task task = Task.builder()
                .title(taskRequest.title())
                .description(taskRequest.description())
                .priority(Priority.PriorityEnum.getById(taskRequest.priority()))
                .status(TaskStatus.TaskStatusEnum.getById(taskRequest.status()))
                .owner((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .executor(userService.getById(taskRequest.executor()))
                .build();
        return saveTask(task);
    }

    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }
    public void deleteTaskRequest(Long id) {
        Task task = getByIdRequest(id);

        boolean isOwner = checkAccessAsOwner(task);

        if (!isOwner) {
            throw new AccessDeniedException("You're not owning this task");
        }

        deleteTask(task);
    }

    public Page<Task> getTasksFiltered(Specification<Task> spec, PageRequest pageRequest) {
        return taskRepository.findAll(spec, pageRequest);
    }
    public Page<Task> getTasksFilteredRequest(TaskController.FilterRequest filter, PageRequest pageRequest) {
        Specification<Task> spec;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUser authUser = (CustomUser) auth.getPrincipal();

        boolean ownerExecutorNotSet = filter.owner() == null && filter.executor() == null;

//        set owner & executor
        Long owner = filter.owner();
        Long executor = filter.executor();
        if (ownerExecutorNotSet && !authUser.isAdmin()) {
            owner = authUser.getUserId();
            executor = authUser.getUserId();
        }
//        set status
        TaskStatus status = null;
        try {
            status = TaskStatus.TaskStatusEnum.getById(filter.status());
        } catch (Exception e) { }
//        set priority
        Priority priority = null;
        try {
            priority = Priority.PriorityEnum.getById(filter.priority());
        } catch (Exception e) { }

//        check if user is owner/executor
        boolean isOwner = userService.checkAccessToUser(owner);
        boolean isExecutor = userService.checkAccessToUser(executor);

        if (!isOwner && !isExecutor) {
            throw new AccessDeniedException("You're neither owning nor executing this task");
        }

//        setting specifications
        spec = Specification.where(TaskSpecifications.filterByOwner(owner));

//        if owner and executor aren't
        if (ownerExecutorNotSet)
            spec = spec.or(TaskSpecifications.filterByExecutor(executor));
        else
            spec = spec.and(TaskSpecifications.filterByExecutor(executor));

//        spec = spec.and(TaskSpecifications.filterByExecutor(executor));
        spec = spec.and(TaskSpecifications.filterByStatus(status));
        spec = spec.and(TaskSpecifications.filterByPriority(priority));

        return getTasksFiltered(spec, pageRequest);
    }

    public boolean checkAccessAsOwner(Task task) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUser authUser = (CustomUser) auth.getPrincipal();

        boolean owner = Objects.equals(task.getOwnerId(), authUser.getUserId());

        return owner || authUser.isAdmin();
    }
    public boolean checkAccessAsExecutor(Task task) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUser authUser = (CustomUser) auth.getPrincipal();

        boolean executor = Objects.equals(task.getExecutorId(), authUser.getUserId());

        return executor || authUser.isAdmin();
    }

    public void addComment(Task task, Comment comment) {
        comment.update();
        task.addComment(comment);
    }
    public Comment addComentRequest(Long id, CommentController.CommentRequest commentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUser user = (CustomUser) authentication.getPrincipal();
        Task task = getByIdRequest(id);
        Optional<Comment> parentComment = commentRepository.findById(commentRequest.parentCommentId());
        Comment comment = Comment
                .builder()
                .content(commentRequest.content())
                .task(task)
                .parentComment(parentComment.get())
                .owner(user)
                .build();
        addComment(task, comment);
        return comment;
    }
}
