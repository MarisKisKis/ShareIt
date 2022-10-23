package ru.practicum.user;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private Long id = 1L;

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findUserById(long userId) { return users.get(userId);}

    @Override
    public User save (User user) {
        user.setId(id);
        users.put(user.getId(), user);
        id++;
        return user;
    }

    @Override
    public User updateUser(long userId, User user) {
        User updatedUser = users.get(userId);
        updatedUser.setId(userId);
        if (user.getEmail() != null) {
            updatedUser.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            updatedUser.setName(user.getName());
        }
        users.put(userId, updatedUser);
        return users.get(userId);
    }

    @Override
    public void deleteUser(long userId){
        users.remove(userId);
    }
}
