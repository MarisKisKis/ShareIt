package ru.practicum.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "text")
    private String text;
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User user;
    @Column(name = "created", nullable = false)
    private LocalDateTime created;

}
