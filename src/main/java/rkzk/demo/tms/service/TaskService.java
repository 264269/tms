package rkzk.demo.tms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rkzk.demo.tms.model.CustomUser;
import rkzk.demo.tms.model.Task;
import rkzk.demo.tms.repository.TaskRepository;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserService userService;

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task getTask(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public List<Task> getTasksByAuthor(Long authorId) {
        return taskRepository.findByOwnerId(authorId);
    }

    public List<Task> getTasksByAssignee(Long assigneeId) {
        return taskRepository.findByExecutorId(assigneeId);
    }

//    public Task updateTask(Task updatedTask) {
//        Task existingTask = getTask(updatedTask.getTaskId());
//
//        if (!existingTask.equals(updatedTask)) {
//            throw new IllegalArgumentException("Invalid task ID");
//        }
//
//        return taskRepository.save(updatedTask);
//    }
//
//    public void deleteTask(Long id) {
//        taskRepository.deleteById(id);
//    }
}
