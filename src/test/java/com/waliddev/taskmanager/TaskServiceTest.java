package com.waliddev.taskmanager;

import com.waliddev.taskmanager.dto.TaskDto;
import com.waliddev.taskmanager.entity.Task;
import com.waliddev.taskmanager.entity.User;
import com.waliddev.taskmanager.exception.ResourceNotFoundException;
import com.waliddev.taskmanager.repository.TaskRepository;
import com.waliddev.taskmanager.repository.UserRepository;
import com.waliddev.taskmanager.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock private TaskRepository taskRepository;
    @Mock private UserRepository userRepository;
    @InjectMocks private TaskService taskService;

    private User testUser;
    private Task testTask;

    @BeforeEach
    void setUp() {
        testUser = User.builder().id(1L).username("walid").email("walid@test.com").build();
        testTask = Task.builder()
                .id(1L).title("Test Task").status(Task.Status.TODO)
                .priority(Task.Priority.MEDIUM).user(testUser).build();

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("walid");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByUsername("walid")).thenReturn(Optional.of(testUser));
    }

    @Test
    void createTask_shouldReturnSavedTask() {
        TaskDto.CreateRequest request = new TaskDto.CreateRequest(
                "New Task", "Description", Task.Priority.HIGH, null);
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        TaskDto.Response response = taskService.createTask(request);

        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("Test Task");
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void getTask_whenNotFound_shouldThrowException() {
        when(taskRepository.findByIdAndUserId(99L, 1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTask(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void getAllTasks_shouldReturnUserTasks() {
        when(taskRepository.findAllByUserIdOrderByCreatedAtDesc(1L)).thenReturn(List.of(testTask));

        List<TaskDto.Response> tasks = taskService.getAllTasks();

        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).getTitle()).isEqualTo("Test Task");
    }

    @Test
    void deleteTask_whenNotOwned_shouldThrowException() {
        when(taskRepository.findByIdAndUserId(99L, 1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.deleteTask(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
