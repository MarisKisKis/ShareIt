package ru.practicum.user;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Email
    @Column(name = "email", nullable = false, unique = true, length = 512)
    private String email;
}
