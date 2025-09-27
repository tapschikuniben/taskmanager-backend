package com.example.usertaskmanager.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateTaskDto {

    // Validation: Ensures 'title' is not null and not just whitespace
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}