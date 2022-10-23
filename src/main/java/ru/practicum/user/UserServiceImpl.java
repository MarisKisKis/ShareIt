package ru.practicum.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exeption.EmptyException;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService{
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<UserDto> getAllUsers() {
        return repository.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(toList());
    }

    @Override
    public UserDto findUserById(long userId) {
        return UserMapper.toUserDto(repository.getById(userId));
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        //validateDupEmail(user);
       // validateEmptyName(user);
        return UserMapper.toUserDto(repository.save(user));
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        User user = repository.getById(userId);
       /*
        if (user.getEmail() != null) {
            validateDupEmail(user);
        }

        */
        return UserMapper.toUserDto(repository.save(user));
    }

    @Override
    public void deleteUser(long userId) {
        User user = repository.getById(userId);
        repository.delete(user);
    }

    /*
    private void validateDupEmail (User valUser) {
        for (User user : repository.findAll()) {
            if (user.getEmail().contains(valUser.getEmail())) {
                throw new EmptyException("Пользователь с таким email уже существует!");
            }
        }
    }
   private void validateEmptyName (User user) {
        if (user.getName().isEmpty()) {
            throw new EmptyException("Имя не может быть пустым!");
        }
    }

     */
}
