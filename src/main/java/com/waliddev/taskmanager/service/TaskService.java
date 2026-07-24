package com.waliddev.taskmanager.service;

import com.waliddev.taskmanager.dto.TaskDto;
import com.waliddev.taskmanager.entity.Task;
import com.waliddev.taskmanager.entity.User;
import com.waliddev.taskmanager.exception.ResourceNotFoundException;
import com.waliddev.taskmanager.repository.TaskRepository;
import com.waliddev.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskService {

    @Autowired private TaskRepository taskRepository;
    @Autowired private UserRepository userRepository;

    private User currentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public List<TaskDto.Response> getAllTasks() {
        return taskRepository.findAllByUserIdOrderByCreatedAtDesc(currentUser().getId())
                .stream().map(TaskDto.Response::from).toList();
    }

    public List<TaskDto.Response> getTasksByStatus(Task.Status status) {
        return taskRepository.findByUserIdAndStatus(currentUser().getId(), status)
                .stream().map(TaskDto.Response::from).toList();
    }

    public TaskDto.Response getTask(Long id) {
        Task task = taskRepository.findByIdAndUserId(id, currentUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        return TaskDto.Response.from(task);
    }

    public TaskDto.Response createTask(TaskDto.CreateRequest request) {
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .dueDate(request.getDueDate())
                .status(Task.Status.TODO)
                .user(currentUser())
                .build();
        return TaskDto.Response.from(taskRepository.save(task));
    }

    public TaskDto.Response updateTask(Long id, TaskDto.UpdateRequest request) {
        Task task = taskRepository.findByIdAndUserId(id, currentUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getStatus() != null) task.setStatus(request.getStatus());
        if (request.getPriority() != null) task.setPriority(request.getPriority());
        if (request.getDueDate() != null) task.setDueDate(request.getDueDate());

        return TaskDto.Response.from(taskRepository.save(task));
    }

    public void deleteTask(Long id) {
        Task task = taskRepository.findByIdAndUserId(id, currentUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        taskRepository.delete(task);
    }
}
