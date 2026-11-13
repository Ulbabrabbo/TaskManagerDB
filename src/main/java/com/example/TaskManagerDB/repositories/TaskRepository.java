package com.example.TaskManagerDB.repositories;

import com.example.TaskManagerDB.data.Status;
import com.example.TaskManagerDB.data.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByStatus(Status status);
}
