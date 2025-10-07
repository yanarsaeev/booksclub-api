package com.booksclub.api.dto;

import com.booksclub.api.entities.Person;
import lombok.Data;

@Data
public class PostDto {
    private String title;

    private String description;

    private Person author;
}
