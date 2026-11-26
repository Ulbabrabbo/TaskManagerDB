package com.example.TaskManagerDB.controller;

import com.example.TaskManagerDB.DTO.TaskResponse;
import com.example.TaskManagerDB.DTO.UpdateTaskRequest;
import com.example.TaskManagerDB.data.Status;
import com.example.TaskManagerDB.data.Task;
import com.example.TaskManagerDB.DTO.CreateTaskRequest;
import com.example.TaskManagerDB.mapper.TaskMapper;
import com.example.TaskManagerDB.service.TaskServiceDB;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskServiceDB myServiceDB;
    private final TaskMapper taskMapper;

    TaskController(TaskServiceDB service, TaskMapper mapper){
        this.myServiceDB = service;
        this.taskMapper = mapper;
    }

    @Operation(summary = "View list of tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service temporarily unavailable")
    })
    @GetMapping
    ResponseEntity<List<TaskResponse>> getTasks(
            @RequestParam(required = false) Status status) {

        List<Task> tasks = (status==null)
                ? myServiceDB.getAllTasks()
                : myServiceDB.filterTasks(status);

        List<TaskResponse> response = tasks.stream()
                .map(taskMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);

    }

    @Operation(summary = "View a single task by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved task"),
            @ApiResponse(responseCode = "404", description = "task not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service temporarily unavailable")
    })
    @GetMapping("/{id}")
    ResponseEntity<TaskResponse> getTaskById(@PathVariable @Positive int id){
        Task taskById = myServiceDB.getTask(id);

        TaskResponse response = taskMapper.toResponse(taskById);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "create new task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created task"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service temporarily unavailable")
    })
    @PostMapping
    ResponseEntity<TaskResponse> postTask(@Valid @RequestBody CreateTaskRequest request){

        // prende il CreateTaskRequest e ne crea un Task con il mapper
        Task task = taskMapper.toEntity(request);
        // aggiunge il task al DB sfruttando il service
        Task taskCreated = myServiceDB.addTask(task);
        // crea la response dal taskCreated sfruttando di nuovo il mapper
        TaskResponse response = taskMapper.toResponse(taskCreated);
        // infine restituisce la response entity mettendo come body la response
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "update existing task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated task"),
            @ApiResponse(responseCode = "404", description = "task not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service temporarily unavailable")
    })
    @PutMapping("/{id}")
    ResponseEntity<TaskResponse> putTask(@Valid @RequestBody UpdateTaskRequest request, @PathVariable @Positive int id){

        // chiamo la updateTask nel service dando in ingresso la request e l'id del task da modificare.
        // restituisce il task aggiornato
        Task updatedTask = myServiceDB.updateTask(id, request);

        // il task aggiornato viene convertito in response e restituito
        return ResponseEntity.ok(taskMapper.toResponse(updatedTask));
    }

    @Operation(summary = "Delete a single task by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted task"),
            @ApiResponse(responseCode = "404", description = "task not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "503", description = "Service temporarily unavailable")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable @Positive int id) {
        myServiceDB.deleteTask(id);
        // nella delete non uso DTO perché non restituisco nessun task infatti ho risposta 204 no content se è tutto ok
        return ResponseEntity.noContent().build();
    }

}
