package com.todo.controller;

import com.todo.dto.ApiResponse;
import com.todo.dto.TodoDto;
import com.todo.security.JwtTokenProvider;
import com.todo.service.TodoService;
import com.todo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TodoController {
    
    private final TodoService todoService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    
    private Long getCurrentUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtTokenProvider.getUsernameFromToken(token);
        return userService.getUserByUsername(username).getId();
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<TodoDto>> createTodo(
            @Valid @RequestBody TodoDto todoDto,
            HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        TodoDto createdTodo = todoService.createTodo(userId, todoDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Todo created successfully", createdTodo));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<TodoDto>>> getAllTodos(
            @RequestParam(required = false) Boolean completed,
            HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        List<TodoDto> todos;
        
        if (completed != null) {
            todos = todoService.getTodosByUserAndStatus(userId, completed);
        } else {
            todos = todoService.getAllTodosByUser(userId);
        }
        
        return ResponseEntity.ok(ApiResponse.success(todos));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoDto>> getTodoById(
            @PathVariable Long id,
            HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        TodoDto todo = todoService.getTodoById(id, userId);
        return ResponseEntity.ok(ApiResponse.success(todo));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoDto>> updateTodo(
            @PathVariable Long id,
            @Valid @RequestBody TodoDto todoDto,
            HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        TodoDto updatedTodo = todoService.updateTodo(id, userId, todoDto);
        return ResponseEntity.ok(ApiResponse.success("Todo updated successfully", updatedTodo));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTodo(
            @PathVariable Long id,
            HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        todoService.deleteTodo(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Todo deleted successfully", null));
    }
    
    @DeleteMapping("/completed")
    public ResponseEntity<ApiResponse<Void>> deleteAllCompleted(
            HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        todoService.deleteAllCompletedTodos(userId);
        return ResponseEntity.ok(ApiResponse.success("All completed todos deleted", null));
    }
    
    @GetMapping("/stats/completed-count")
    public ResponseEntity<ApiResponse<Long>> getCompletedCount(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        long count = todoService.getCompletedTodosCount(userId);
        return ResponseEntity.ok(ApiResponse.success(count));
    }
}

