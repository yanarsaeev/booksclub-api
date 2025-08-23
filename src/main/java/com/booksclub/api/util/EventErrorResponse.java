package com.booksclub.api.util;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventErrorResponse {
    private int statusCode;
    private String message;
}
