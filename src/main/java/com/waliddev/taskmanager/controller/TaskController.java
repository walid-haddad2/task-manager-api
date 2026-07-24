package com.waliddev.taskmanager.controller;

import com.waliddev.taskmanager.dto.TaskDto;
import com.waliddev.taskmanager.entity.Task;
import com.waliddev.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Tasks", description = "CRUD operations for tasks")
public class TaskController {

    @Autowired private TaskService taskService;

    @GetMapping
    @Operation(summary = "Get all tasks for the authenticated user")
    public ResponseEntity<List<TaskDto.Response>> getAllTasks(
            @RequestParam(required = false) Task.Status status) {
        if (status != null) {
            return ResponseEntity.ok(taskService.getTasksByStatus(status));
        }
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a task by ID")
    public ResponseEntity<TaskDto.Response> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTask(id));
    }

    @PostMapping
    @Operation(summary = "Create a new task")
    public ResponseEntity<TaskDto.Response> createTask(@Valid @RequestBody TaskDto.CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing task")
    public ResponseEntity<TaskDto.Response> updateTask(@PathVariable Long id,
                                                       @Valid @RequestBody TaskDto.UpdateRequest request) {
        return ResponseEntity.ok(taskService.updateTask(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
