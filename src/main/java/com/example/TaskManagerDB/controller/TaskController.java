package com.example.TaskManagerDB.controller;

import com.example.TaskManagerDB.data.Status;
import com.example.TaskManagerDB.data.Task;
import com.example.TaskManagerDB.data.TaskRequest;
import com.example.TaskManagerDB.service.TaskServiceDB;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskServiceDB myServiceDB;

    TaskController(TaskServiceDB service){
        this.myServiceDB = service;
    }

    @Operation(summary = "View list of tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to access this resource"),
            @ApiResponse(responseCode = "404", description = "List not found")
    })
    @GetMapping
    public ResponseEntity<List<Task>> getTasks(
            @RequestParam(required = false) Status status) {
        if (status == null) {
            return ResponseEntity.ok(myServiceDB.getAllTasks());
        }
        return ResponseEntity.ok(myServiceDB.filterTasks(status));
    }

    @Operation(summary = "View a single task by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved task"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to access this resource"),
            @ApiResponse(responseCode = "404", description = "task not found")
    })
    @GetMapping("/{id}")
    ResponseEntity<Task> getTaskById(@PathVariable int id){
        Task taskById = myServiceDB.getTask(id);
        return ResponseEntity.ok(taskById);
    }

    @Operation(summary = "create new task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created task"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to access this resource")
    })
    @PostMapping
    ResponseEntity<Task> postTask(@Valid @RequestBody TaskRequest request){
        Task task = new Task();

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());

        Task taskCreated = myServiceDB.addTask(task);

        return ResponseEntity.status(HttpStatus.CREATED).body(taskCreated);
    }

    @Operation(summary = "update existing task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated task"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to access this resource"),
            @ApiResponse(responseCode = "404", description = "task not found")
    })
    @PutMapping("/{id}")
    ResponseEntity<Task> putTask(@Valid @RequestBody TaskRequest request, @PathVariable int id){
        Task taskModified = new Task();

        taskModified.setTitle(request.getTitle());
        taskModified.setDescription(request.getDescription());
        taskModified.setStatus(request.getStatus());

        Task updatedTask = myServiceDB.updateTask(id, taskModified);

        return ResponseEntity.ok(updatedTask);
    }

    @Operation(summary = "View a single task by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted task"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to access this resource"),
            @ApiResponse(responseCode = "404", description = "task not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable int id) {
        myServiceDB.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

}
