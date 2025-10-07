package com.booksclub.api.service;

import com.booksclub.api.entities.Role;
import com.booksclub.api.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public Role getUserRole() {
        return roleRepository.findByName("ROLE_BRONZE").get();
    }
}
