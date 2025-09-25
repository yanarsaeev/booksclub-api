package com.booksclub.api.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PersonDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
