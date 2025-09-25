package com.booksclub.api.service;

import com.booksclub.api.dto.RegistrationUserDto;
import com.booksclub.api.entities.Person;
import com.booksclub.api.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PersonService implements UserDetailsService {

    private PersonRepository personRepository;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserRepository(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Person> findPersonByEmail(String email) {
        return this.personRepository.findPersonByEmail(email);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.personRepository.findPersonByEmail(email)
                .map(person -> new User(
                        person.getEmail(),
                        person.getPassword(),
                        person.getRoles().stream().map(role ->
                                new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet())
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Username not found."));
    }

    public Person createNewPerson(RegistrationUserDto registrationUserDto) {
        Person person = new Person();
        person.setFirst_name(registrationUserDto.getFirstName());
        person.setLastName(registrationUserDto.getLastName());
        person.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        person.setEmail(registrationUserDto.getEmail());
        person.setRoles(Set.of(roleService.getUserRole()));
        return personRepository.save(person);
    }
}
