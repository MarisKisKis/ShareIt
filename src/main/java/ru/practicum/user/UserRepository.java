package ru.practicum.user;

import java.util.List;


public interface UserRepository {

    List<User> findAll();

    User save(User user);

    User updateUser(long userId, User user);

    User findUserById(long userId);

    void deleteUser(long userId);
}
