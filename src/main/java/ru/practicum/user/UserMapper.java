package ru.practicum.user;

public class UserMapper {
    public static User toUser(UserDto dto) {
        return new User(dto.getId(), dto.getName(), dto.getEmail());
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }
}
