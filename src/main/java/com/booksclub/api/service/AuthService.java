package com.booksclub.api.service;

import com.booksclub.api.dto.JwtRequest;
import com.booksclub.api.dto.JwtResponse;
import com.booksclub.api.dto.PersonDto;
import com.booksclub.api.dto.RegistrationUserDto;
import com.booksclub.api.entities.Person;
import com.booksclub.api.entities.PersonCreatedEvent;
import com.booksclub.api.exception.AppError;
import com.booksclub.api.util.JwtTokenUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class AuthService {

    private PersonService peopleService;
    private JwtTokenUtils jwtTokenUtils;
    private AuthenticationManager authenticationManager;
    private KafkaTemplate<String, PersonCreatedEvent> kafkaTemplate;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsername(), request.getPassword()));
        } catch (BadCredentialsException exception) {
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(),
                    "Incorrect login or password"), HttpStatus.UNAUTHORIZED);
        }

        UserDetails userDetails = peopleService.loadUserByUsername(request.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public ResponseEntity<?> createNewUser(RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Пароли не совпадают"),
                    HttpStatus.BAD_REQUEST);
        }
        if (peopleService.findPersonByEmail(registrationUserDto.getEmail()).isPresent()) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(),
                    "Пользователь с указанным email уже существует."),
                    HttpStatus.BAD_REQUEST);
        }

        String userId = UUID.randomUUID().toString();
        PersonCreatedEvent personCreatedEvent = new PersonCreatedEvent(userId, registrationUserDto.getEmail());
        CompletableFuture<SendResult<String, PersonCreatedEvent>> future = kafkaTemplate
                .send("person-created-topic", userId, personCreatedEvent);

        future.whenComplete((result, exception) -> {
           if (exception != null) {
               LOGGER.error("Failed to send message: {}", exception.getMessage());
           } else {
                LOGGER.info("Message sent successfully: {}", result.getRecordMetadata());
           }
        });

        LOGGER.info("Return: {}", userId);

        Person person = peopleService.createNewPerson(registrationUserDto);
        return ResponseEntity.ok(new PersonDto(person.getId(), person.getFirst_name(),
                person.getLastName(), person.getEmail()));
    }
}
