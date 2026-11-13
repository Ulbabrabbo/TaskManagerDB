package com.example.TaskManagerDB.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskRequest {

    @NotBlank
    private String title;

    private String description;

    @NotNull
    private Status status;
}