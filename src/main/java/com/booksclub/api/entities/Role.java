package com.booksclub.api.entities;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "role_name")
    private String name;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private Set<User> users;

    private void addUser(User user) {
        users.add(user);
        user.setRole(this);
    }

    private void removeUser(User user) {
        users.remove(user);
        user.setRole(null);
    }
}
