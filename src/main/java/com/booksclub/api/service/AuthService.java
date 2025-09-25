package com.booksclub.api.service;

import com.booksclub.api.dto.JwtRequest;
import com.booksclub.api.dto.JwtResponse;
import com.booksclub.api.dto.PersonDto;
import com.booksclub.api.dto.RegistrationUserDto;
import com.booksclub.api.entities.Person;
import com.booksclub.api.exception.AppError;
import com.booksclub.api.util.JwtTokenUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@AllArgsConstructor
public class AuthService {

    private PersonService peopleService;
    private JwtTokenUtils jwtTokenUtils;
    private AuthenticationManager authenticationManager;


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
        Person person = peopleService.createNewPerson(registrationUserDto);
        return ResponseEntity.ok(new PersonDto(person.getId(), person.getFirst_name(),
                person.getLastName(), person.getEmail()));
    }
}
