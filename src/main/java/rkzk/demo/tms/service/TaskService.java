package rkzk.demo.tms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rkzk.demo.tms.controller.TaskController;
import rkzk.demo.tms.model.CustomUser;
import rkzk.demo.tms.model.Task;
import rkzk.demo.tms.model.persistent.Priority;
import rkzk.demo.tms.model.persistent.TaskStatus;
import rkzk.demo.tms.repository.TaskRepository;

import java.util.List;
import java.util.Objects;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserService userService;

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task getById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
    }
    public Task getByIdRequest(Long id) {
        Task task = getById(id);
        if (!checkAccessAsOwner(task) && !checkAccessAsExecutor(task)) {
            System.out.println(!checkAccessAsOwner(task) + " " + !checkAccessAsExecutor(task));
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
        System.out.println("Saving task");
        return taskRepository.save(task);
    }
    public Task updateTaskRequest(TaskController.TaskRequest taskRequest, Long taskId) {
        Task task = getById(taskId);

        boolean isOwner = checkAccessAsOwner(task);
        boolean isExecutor = checkAccessAsExecutor(task);

        if (!isOwner && !isExecutor) {
            throw new AccessDeniedException("You're neither executing nor owning this task");
        }

        //set title
        if (isOwner && !Objects.isNull(taskRequest.title())) {
            System.out.println("Trying to set title:");
            task.setTitle(taskRequest.title());
            System.out.println("Title set to " + task.getTitle());
        }

        //set description
        if (isOwner && !Objects.isNull(taskRequest.description())) {
            System.out.println("Trying to set description:");
            task.setDescription(taskRequest.description());
            System.out.println("Description set to " + task.getDescription());
        }

        //set priority
        if (isOwner)
            try {
                System.out.println("Trying to set priority:");
                task.setPriority(
                        Priority.PriorityEnum.getById(
                                taskRequest.priorityId()));
                System.out.println("Priority set to " + task.getPriority());
            } catch (Exception e) { }

        //set status
        try {
            System.out.println("Trying to set status:");
            task.setStatus(
                    TaskStatus.TaskStatusEnum.getById(
                            taskRequest.statusId()));
            System.out.println("Status set to " + task.getStatus());
        } catch (Exception e) { }

        //set executor
        if (isOwner)
            try {
                System.out.println("Trying to set executor:");
                task.setExecutor(userService.getById(taskRequest.executorId()));
                System.out.println("Executor set to " + task.getExecutorId());
            } catch (Exception e) { }

        return saveTask(task);
    }
    public Task createTaskRequest(TaskController.TaskRequest taskRequest) {
        Task task = Task.builder()
                .title(taskRequest.title())
                .description(taskRequest.description())
                .priority(Priority.PriorityEnum.getById(taskRequest.priorityId()))
                .status(TaskStatus.TaskStatusEnum.getById(taskRequest.statusId()))
                .owner((CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .executor(userService.getById(taskRequest.executorId()))
                .build();
        return saveTask(task);
    }

    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }
    public void deleteTaskRequest(Long id) {
        Task task = getById(id);
        boolean isOwner = checkAccessAsOwner(task);

        if (!isOwner) {
            throw new AccessDeniedException("You're not executing this task");
        }

        deleteTask(task);
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
//
//    public void deleteTask(Long id) {
//        taskRepository.deleteById(id);
//    }
}
