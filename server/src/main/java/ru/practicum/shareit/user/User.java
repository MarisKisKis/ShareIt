package ru.practicum.shareit.user;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;


@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;
    @Email
    @Column(name = "email", nullable = false, unique = true, length = 512)
    private String email;
    @Column(name = "name", nullable = false)
    private String name;

    public User(String email, String name) {
        this.email = email;
        this.name = name;

    }
}
