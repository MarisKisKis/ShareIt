package ru.practicum.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items", schema = "public")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;
    @NotNull
    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;
    @NotNull
    @NotBlank
    @Column(name = "description", nullable = false)
    private String description;
    @NotNull
    @Column(name = "available", nullable = false)
    private boolean available;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Item (String name, String description, Boolean available, User user) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.user = user;

    }

    /*
    @Column(name = "request_Id")
    private String requestId;

     */
}
