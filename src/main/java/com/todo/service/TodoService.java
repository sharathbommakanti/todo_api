package com.todo.service;

import com.todo.dto.TodoDto;
import com.todo.entity.Todo;
import com.todo.entity.User;
import com.todo.exception.ResourceNotFoundException;
import com.todo.exception.UnauthorizedException;
import com.todo.repository.TodoRepository;
import com.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoService {
    
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public TodoDto createTodo(Long userId, TodoDto todoDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Todo todo = new Todo();
        todo.setText(todoDto.getText());
        todo.setCompleted(todoDto.isCompleted());
        todo.setUser(user);
        
        Todo savedTodo = todoRepository.save(todo);
        return mapToDto(savedTodo);
    }
    
    @Transactional(readOnly = true)
    public List<TodoDto> getAllTodosByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        return todoRepository.findByUser(user).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<TodoDto> getTodosByUserAndStatus(Long userId, Boolean completed) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        return todoRepository.findByUserAndCompleted(user, completed).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public TodoDto getTodoById(Long todoId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Todo todo = todoRepository.findByIdAndUser(todoId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + todoId));
        
        return mapToDto(todo);
    }
    
    @Transactional
    public TodoDto updateTodo(Long todoId, Long userId, TodoDto todoDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Todo todo = todoRepository.findByIdAndUser(todoId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + todoId));
        
        if (todoDto.getText() != null) {
            todo.setText(todoDto.getText());
        }
        todo.setCompleted(todoDto.isCompleted());
        
        Todo updatedTodo = todoRepository.save(todo);
        return mapToDto(updatedTodo);
    }
    
    @Transactional
    public void deleteTodo(Long todoId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Todo todo = todoRepository.findByIdAndUser(todoId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + todoId));
        
        todoRepository.delete(todo);
    }
    
    @Transactional
    public void deleteAllCompletedTodos(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        todoRepository.deleteByUserAndCompleted(user, true);
    }
    
    @Transactional(readOnly = true)
    public long getCompletedTodosCount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        return todoRepository.countByUserAndCompleted(user, true);
    }
    
    private TodoDto mapToDto(Todo todo) {
        TodoDto dto = new TodoDto();
        dto.setId(todo.getId());
        dto.setText(todo.getText());
        dto.setCompleted(todo.isCompleted());
        dto.setCreatedAt(todo.getCreatedAt());
        dto.setUpdatedAt(todo.getUpdatedAt());
        dto.setUserId(todo.getUser().getId());
        return dto;
    }
}

