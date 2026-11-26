package com.example.TaskManagerDB.service;

import com.example.TaskManagerDB.DTO.UpdateTaskRequest;
import com.example.TaskManagerDB.data.Status;
import com.example.TaskManagerDB.data.Task;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TaskService {

    List<Task> getAllTasks();

    Task getTask(Integer id);

    Task addTask(Task task);

    Task updateTask(int id, UpdateTaskRequest request);

    void deleteTask(int id);

    List<Task> filterTasks(Status status);
}
