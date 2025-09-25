package com.booksclub.api.repository;

import com.booksclub.api.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findPersonByEmail(String email);
}
