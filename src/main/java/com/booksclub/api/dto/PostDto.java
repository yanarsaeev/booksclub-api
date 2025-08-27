package com.booksclub.api.dto;

import com.booksclub.api.entities.User;
import lombok.Data;

@Data
public class PostDto {
    private String title;

    private String description;

    private User author;
}
