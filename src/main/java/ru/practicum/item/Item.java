package ru.practicum.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    @Column(name = "item_name", nullable = false)
    private String name;
    @NotNull
    @NotBlank
    @Column(name = "description", nullable = false)
    private String description;
    @NotNull
    @Column(name = "available", nullable = false)
    private boolean available;
    @NotNull
    @Column(name = "user_Id", nullable = false)
    private Long userId;
    @Column(name = "request_Id")
    private String requestId;
}
