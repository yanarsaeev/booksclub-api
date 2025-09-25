package com.booksclub.api.dto;

import lombok.Data;

@Data
public class RegistrationUserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;
}
