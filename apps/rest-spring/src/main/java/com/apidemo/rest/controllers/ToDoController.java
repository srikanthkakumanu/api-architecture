package com.apidemo.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.apidemo.rest.service.ToDoService;
import com.apidemo.rest.validation.ToDoValidationErrorBuilder;
import com.apidemo.rest.validation.ToDoValidationError;

import java.net.URI;

import jakarta.validation.Valid;

import com.apidemo.rest.builders.ToDoBuilder;
import com.apidemo.rest.model.ToDo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api")
public class ToDoController {
    private static final Logger log = LoggerFactory.getLogger(ToDoController.class.getCanonicalName());
    private ToDoService service;

    /**
     * @Autowired meaning it injects ToDoService<ToDo> implementation.
     * However, it can be omitted. Spring automatically injects any declared
     * dependency since 4.3.
     * @param repository
     */
    @Autowired  
    public ToDoController(ToDoService service) { this.service = service; }

    /**
     * GET: Returns all ToDo's
     * @return
     */
    @GetMapping("/todo")
    public ResponseEntity<Iterable<ToDo>> getToDos() {
        log("/todo", "GET", "getToDos() called"); 
        return ResponseEntity.ok(service.getToDos());
    }

    /**
     * GET: Return a ToDo by Id
     * 
     * @GetMapping: is shortcut version of @RequestMapping equivalent to below.
     * @RequestMapping(value="/todo", method={RequestMethod.GET}).
     * 
     * @PathVariable: helps in declaring an endpoint that contains a URL expression.
     * e.g. /api/todo/{id}
     * Note: ID must match the name of the method parameter.
     * 
     * @param id
     * @return
     */
    @GetMapping("/todo/{id}")
    public ResponseEntity<ToDo> getToDoById(@PathVariable String id) {
        log("/todo/{id}", "GET", "getToDoById() called");
        return ResponseEntity.ok(service.getToDoById(id));
    }

    /**
     * PATCH (UPDATE): Sets or updates completed status of a ToDo
     * @param id
     * @return
     */
    @PatchMapping("/todo/{id}")
    public ResponseEntity<ToDo> setCompleted(@PathVariable String id) {
        log("/todo/{id}", "PATCH", "setCompleted() called");
        ToDo result = service.getToDoById(id);
        result.setCompleted(true);
        service.saveToDo(result);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                                .buildAndExpand(result.getId())
                                                .toUri();
        return ResponseEntity.ok().header("Location", location.toString()).build();
    }

    /**
     * Creates or Saves a ToDo
     * @param todo
     * @param errors
     * @return
     */
    @RequestMapping(value="/todo", method={RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<?> createToDo(@Valid @RequestBody ToDo todo, Errors errors) {
        log("/todo", "POST or PUT", "createToDo() called");
        if(errors.hasErrors()) 
            return ResponseEntity.badRequest()
                                .body(ToDoValidationErrorBuilder.fromBindingErrors(errors));
        
        ToDo result = service.saveToDo(todo);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                                                .buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    /**
     * DELETE: Deletes a ToDo by Id
     * @param id
     * @return
     */
    @DeleteMapping("/todo/{id}")
    public ResponseEntity<ToDo> deleteToDo(@PathVariable String id) {
        log("/todo/{id}", "DELETE", "deleteToDo(id) called");
        
        //service.deleteToDo(ToDoBuilder.create().withId(id).build());
        service.deleteToDo(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * DELETE: Deletes a ToDo model object
     * @param todo
     * @return
     */
    @DeleteMapping("/todo")
    public ResponseEntity<ToDo> deleteToDo(@RequestBody ToDo todo) {
        log("/todo", "DELETE", "deleteToDo(ToDo) called");
        service.deleteToDo(todo);
        return ResponseEntity.noContent().build();
    }

    /**
     * Exception handler method for ToDoController
     * @param exception
     * @return
     */
    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ToDoValidationError handleException(Exception exception) {
        log.error("Exception caught: ", exception.getMessage());
        return new ToDoValidationError(exception.getMessage());
    }

    /**
     * Simple util method to log message
     * @param uri
     * @param method
     * @param message
     */
    private static void log(String uri, String method, String message) {
        log.info(String.format("[URI: %s, Method: %s] = %s", uri, method, message));
    }
}
