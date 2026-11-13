package com.example.TaskManagerDB.service;

import com.example.TaskManagerDB.data.Status;
import com.example.TaskManagerDB.data.Task;
import com.example.TaskManagerDB.exception.TaskNotFoundException;
import com.example.TaskManagerDB.repositories.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceDB implements TaskService {

    private final TaskRepository myRepository;

    TaskServiceDB(TaskRepository repository){
        this.myRepository = repository;
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
    public Task updateTask(int id, Task task) {
        Task existingTask = myRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setStatus(task.getStatus());

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
