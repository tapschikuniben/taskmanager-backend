package com.example.usertaskmanager.controller;

import com.example.usertaskmanager.dto.CreateTaskDto;
import com.example.usertaskmanager.model.Task;
import com.example.usertaskmanager.model.TaskStatus;
import com.example.usertaskmanager.model.User;
import com.example.usertaskmanager.service.TaskService;
import com.example.usertaskmanager.config.JwtUtil;
import com.example.usertaskmanager.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public TaskController(TaskService taskService, JwtUtil jwtUtil, UserRepository userRepository) {
        this.taskService = taskService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    private User getUserFromToken(String token) {
        UUID userId = jwtUtil.getUserIdFromToken(token);
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestHeader("Authorization") String authHeader,
                                           @RequestBody @Valid CreateTaskDto dto) {
        String token = authHeader.replace("Bearer ", "");
        User user = getUserFromToken(token);
        Task task = taskService.createTask(dto, user);
        return ResponseEntity.status(201).body(task);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getTasks(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        User user = getUserFromToken(token);
        List<Task> tasks = taskService.getTasks(user);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@RequestHeader("Authorization") String authHeader,
                                        @PathVariable UUID id) {
        User user = getUserFromToken(authHeader.replace("Bearer ", ""));
        Task task = taskService.getTaskById(id, user);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@RequestHeader("Authorization") String authHeader,
                                           @PathVariable UUID id,
                                           @RequestBody @Valid CreateTaskDto dto) {
        User user = getUserFromToken(authHeader.replace("Bearer ", ""));
        Task updatedTask = taskService.updateTask(id, dto, user);
        return ResponseEntity.ok(updatedTask);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Task> updateTaskStatus(@RequestHeader("Authorization") String authHeader,
                                                 @PathVariable UUID id,
                                                 @RequestParam TaskStatus status) {
        User user = getUserFromToken(authHeader.replace("Bearer ", ""));
        Task updatedTask = taskService.updateTaskStatus(id, status, user);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteTask(@RequestHeader("Authorization") String authHeader,
                                                          @PathVariable UUID id) {
        User user = getUserFromToken(authHeader.replace("Bearer ", ""));
        taskService.deleteTask(id, user);
        return ResponseEntity.ok(Map.of("message", "Task deleted successfully"));
    }

}
