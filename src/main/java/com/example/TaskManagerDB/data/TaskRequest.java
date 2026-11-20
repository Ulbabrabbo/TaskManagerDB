package com.example.TaskManagerDB.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskRequest {

    @NotBlank(message = "titolo richiesto!")
    private String title;

    private String description;

    @NotNull(message = "Status richiesto!")
    private Status status;
}