package com.waliddev.taskmanager.repository;

import com.waliddev.taskmanager.entity.Task;
import com.waliddev.taskmanager.entity.Task.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long userId);
    List<Task> findByUserIdAndStatus(Long userId, Status status);
    Optional<Task> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT t FROM Task t WHERE t.user.id = :userId ORDER BY t.createdAt DESC")
    List<Task> findAllByUserIdOrderByCreatedAtDesc(Long userId);
}
