package ru.practicum.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exeption.ObjectNotFoundException;
import ru.practicum.exeption.ValidationException;

import java.util.List;
import java.util.Optional;


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
        Optional<User> user = repository.findById(userId);
        if (user == null) {
            throw new ObjectNotFoundException("Нет такого пользователя");
        }
        return UserMapper.toUserDto(user.get());
    }

    @Transactional
    @Override
    public UserDto saveUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Некорректный Email");
        }
        return UserMapper.toUserDto(repository.save(user));
    }

    @Transactional
    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        User user = repository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("Нет такого пользователя!"));
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        return UserMapper.toUserDto(repository.save(user));
    }

    @Transactional
    @Override
    public void deleteUser(long userId) {
        repository.deleteById(userId);
    }
}
