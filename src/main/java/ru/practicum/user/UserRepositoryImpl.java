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
    private final Map<Long, UserDto> users = new HashMap<>();
    private Long id = 1L;

    @Override
    public List<UserDto> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public UserDto findUserById(long userId) { return users.get(userId);}

    @Override
    public UserDto save (UserDto user) {
        user.setId(id);
        users.put(user.getId(), user);
        id++;
        return user;
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        UserDto updatedUser = users.get(userId);
        updatedUser.setId(userId);
        if (userDto.getEmail() != null) {
            updatedUser.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null) {
            updatedUser.setName(userDto.getName());
        }
        users.put(userId, updatedUser);
        return users.get(userId);
    }

    @Override
    public void deleteUser(long userId){
        users.remove(userId);
    }
}
