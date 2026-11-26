package com.example.TaskManagerDB.service;

import com.example.TaskManagerDB.DTO.UpdateTaskRequest;
import com.example.TaskManagerDB.data.Status;
import com.example.TaskManagerDB.data.Task;
import com.example.TaskManagerDB.exception.TaskNotFoundException;
import com.example.TaskManagerDB.mapper.TaskMapper;
import com.example.TaskManagerDB.repositories.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceDB implements TaskService {

    private final TaskRepository myRepository;
    private final TaskMapper myMapper;

    TaskServiceDB(TaskRepository repository, TaskMapper mapper){
        this.myRepository = repository;
        this.myMapper = mapper;
    }

    @Override
    public List<Task> getAllTasks() {
        return myRepository.findAll();
    }

    @Override
    public Task getTask(Integer id) {
        return myRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Override
    public Task addTask(Task task) {
        myRepository.save(task);
        return task;
    }

    @Override
    public Task updateTask(int id, UpdateTaskRequest request) {
        // controllo che ci sia il task con dato id
        Task existingTask = myRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        // il mapper va a mappare i valori del request fornito nel existingTask (MappingTarget)
        myMapper.updateEntity(request, existingTask);
        // restituisce un task per poi creare la response (existingTak ormai è modificato)
        return myRepository.save(existingTask);
    }

    @Override
    public void deleteTask(int id) {
        if (!myRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }

        myRepository.deleteById(id);
    }

    @Override
    public List<Task> filterTasks(Status status) {
        return myRepository.findByStatus(status);
    }

}
