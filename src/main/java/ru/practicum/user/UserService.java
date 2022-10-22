package ru.practicum.user;

import java.util.List;

public interface UserService {
    List <UserDto> getAllUsers();

    UserDto findUserById(long userId);

    UserDto saveUser(UserDto userDto);

    UserDto updateUser(long userId, UserDto userDto);

    void deleteUser(long userId);
}
