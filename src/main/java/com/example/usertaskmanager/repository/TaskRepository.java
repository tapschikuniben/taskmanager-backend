package com.example.usertaskmanager.repository;

import com.example.usertaskmanager.model.Task;
import com.example.usertaskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findAllByUser(User user);
    Optional<Task> findByIdAndUser(UUID id, User user);
}
