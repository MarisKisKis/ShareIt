package ru.practicum.item;

public class CommentMapper {

    public static Comment toComment(CommentDto dto) {
        return new Comment(dto.getId(), dto.getText(), dto.getItemId(), dto.getAuthorId(), dto.getCreated());
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getItemId(), comment.getAuthorId(), comment.getCreated());
    }
}
