package com.booksclub.api.entities;

public class PersonCreatedEvent {
    private String id;
    private String email;

    public PersonCreatedEvent() {
    }

    public PersonCreatedEvent(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }
}
