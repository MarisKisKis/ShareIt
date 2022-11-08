package ru.practicum.shareit.user;


public class UserMapper {
    public static User toUser(UserDto dto) {
        return new User(dto.getEmail(), dto.getName());
    }
    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getEmail(), user.getName());
    }
}
