package com.example.TaskManagerDB.DTO;

import com.example.TaskManagerDB.data.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTaskRequest (
        @NotBlank(message = "titolo richiesto!")
        @Size(message = "il titolo deve essere massimo di 100 caratteri!", max = 100)

        String title,

        @Size(message = "la descrizione deve essere massimo di 200 caratteri!", max = 200)
        String description,

        @NotNull(message = "Status richiesto!")
        Status status
        ){}