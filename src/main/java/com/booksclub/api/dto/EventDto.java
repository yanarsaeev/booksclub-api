package com.booksclub.api.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventDto {

    @Size(max = 50, message = "The length cannot be more than 50 characters.")
    private String name;

    @Size(max = 300, message = "The length cannot be more than 300 characters.")
    private String description;

    private LocalDateTime plannedAt;
}
