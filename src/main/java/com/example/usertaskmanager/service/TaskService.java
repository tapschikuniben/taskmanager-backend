package com.example.usertaskmanager.service;

import com.example.usertaskmanager.dto.CreateTaskDto;
import com.example.usertaskmanager.exception.TaskNotFoundException;
import com.example.usertaskmanager.model.Task;
import com.example.usertaskmanager.model.TaskStatus;
import com.example.usertaskmanager.model.User;
import com.example.usertaskmanager.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(CreateTaskDto dto, User user) {
        Task task = new Task(dto.getTitle(), dto.getDescription(), user);
        Task saved = taskRepository.save(task);
        logger.info("Task created: {} by user: {}", saved.getId(), user.getUsername());
        return saved;
    }

    public List<Task> getTasks(User user) {
        List<Task> tasks = taskRepository.findAllByUser(user);
        logger.info("Retrieved {} tasks for user: {}", tasks.size(), user.getUsername());
        return tasks;
    }

    public Task getTaskById(UUID taskId, User user) {
        return taskRepository.findByIdAndUser(taskId, user)
                .orElseThrow(() -> {
                    logger.warn("Task not found: {} for user: {}", taskId, user.getUsername());
                    return new TaskNotFoundException("Task not found with ID: " + taskId);
                });
    }

    public Task updateTask(UUID taskId, CreateTaskDto dto, User user) {
        Task task = getTaskById(taskId, user); // throws if not found
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        Task updated = taskRepository.save(task);
        logger.info("Task updated: {} by user: {}", updated.getId(), user.getUsername());
        return updated;
    }

    public void deleteTask(UUID taskId, User user) {
        Task task = getTaskById(taskId, user); // throws if not found
        taskRepository.delete(task);
        logger.info("Task deleted: {} by user: {}", task.getId(), user.getUsername());
    }

    public Task updateTaskStatus(UUID taskId, TaskStatus status, User user) {
        Task task = getTaskById(taskId, user); // throws if not found
        task.setStatus(status);
        Task updated = taskRepository.save(task);
        logger.info("Task status updated: {} to {} by user: {}", updated.getId(), status, user.getUsername());
        return updated;
    }
}
