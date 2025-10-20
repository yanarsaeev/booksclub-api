package com.booksclub.api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_name")
    @Size(max = 30, message = "The length cannot be more than 50 characters.")
    private String name;

    @Size(max = 300, message = "The length cannot be more than 300 characters.")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "users_id")
    private Person manager;

    @Column(name = "planned_at")
    private LocalDateTime plannedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) && Objects.equals(name, event.name)
                && Objects.equals(description, event.description) && Objects.equals(manager, event.manager)
                && Objects.equals(plannedAt, event.plannedAt) && Objects.equals(createdAt, event.createdAt)
                && Objects.equals(updatedAt, event.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, manager, plannedAt, createdAt, updatedAt);
    }
}
