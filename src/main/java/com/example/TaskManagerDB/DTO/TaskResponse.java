package com.example.TaskManagerDB.DTO;

import com.example.TaskManagerDB.data.Status;

public record TaskResponse (int id,
                            String title,
                            String description,
                            Status status
)
{}