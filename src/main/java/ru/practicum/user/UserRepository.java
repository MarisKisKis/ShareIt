package ru.practicum.user;

import java.util.List;


public interface UserRepository {
    List<UserDto> findAll();
    UserDto save(UserDto userDto);
    UserDto updateUser(long userId, UserDto userDto);
    UserDto findUserById(long userId);
    void deleteUser(long userId);
}
