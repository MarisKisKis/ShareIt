package ru.practicum.item;

import ru.practicum.user.User;

import java.time.LocalDateTime;

public class CommentMapper {

    public static Comment toComment(CommentDto dto, Item item, User author, LocalDateTime localDateTime) {
        return new Comment(dto.getId(), dto.getText(), item, author, localDateTime);
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getItem(), comment.getUser().getName(), comment.getCreated());
    }
}
