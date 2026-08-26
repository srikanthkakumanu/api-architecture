package com.apidemo.rest.service;

import org.springframework.stereotype.Service;
import com.apidemo.rest.exception.NotFoundException;
import com.apidemo.rest.repos.CommonRepository;
import com.apidemo.rest.model.ToDo;

@Service
public class ToDoService {
    private final CommonRepository<ToDo> repository;

    public ToDoService(CommonRepository<ToDo> repository) { this.repository = repository; }

    public Iterable<ToDo> getToDos() { 
        return repository.findAll();
    }

    public ToDo getToDoById(String id) {
        return repository.findById(id);
    }

    public void setCompleted(String id) { 
        ToDo todo = repository.findById(id);
        if(todo != null) {
            todo.setCompleted(true);
            repository.save(todo);
        }
        else
            throw new NotFoundException(String.format("ToDo not found %s", id));
    }

    public ToDo saveToDo(ToDo todo) { 
        return repository.save(todo);
    }

    public ToDo deleteToDo(String id) { 
        ToDo todo = repository.findById(id);
        if(todo != null) {
            repository.delete(todo);
        }
        return todo;
    }

    public void deleteToDo(ToDo todo) { 
        repository.delete(todo);
    }
}
