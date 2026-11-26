package com.example.TaskManagerDB.mapper;

import com.example.TaskManagerDB.DTO.CreateTaskRequest;
import com.example.TaskManagerDB.DTO.TaskResponse;
import com.example.TaskManagerDB.DTO.UpdateTaskRequest;
import com.example.TaskManagerDB.data.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task toEntity(CreateTaskRequest request);
    void updateEntity(UpdateTaskRequest request, @MappingTarget Task task);

    TaskResponse toResponse(Task task);

}
