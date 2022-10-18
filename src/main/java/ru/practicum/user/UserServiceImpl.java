package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exeption.EmptyException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository repository;

    @Override
    public List<UserDto> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public UserDto findUserById(long userId) {return repository.findUserById(userId);}

    @Override
    public UserDto saveUser(UserDto userDto) {
        validateDupEmail(userDto);
        validateEmptyName(userDto);
        return repository.save(userDto);
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        if (userDto.getEmail() != null) {
            validateDupEmail(userDto);
        }
        return repository.updateUser(userId, userDto);
    }

    @Override
    public void deleteUser(long userId) {repository.deleteUser(userId);}

    private void validateDupEmail (UserDto userDto) {
        for (UserDto user : repository.findAll()) {
            if (user.getEmail().contains(userDto.getEmail())) {
                throw new EmptyException("Пользователь с таким email уже существует!");
            }
        }
    }
   private void validateEmptyName (UserDto userDto) {
        if (userDto.getName().isEmpty()) {
            throw new EmptyException("Имя не может быть пустым!");
        }
    }
}
