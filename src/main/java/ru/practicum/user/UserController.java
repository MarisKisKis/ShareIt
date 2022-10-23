package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Create;
import ru.practicum.Update;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController (UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List <UserDto> getAllUsers() {
        log.info("Получаем всех пользователей");
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDto findUserById(@PathVariable Long userId) {
        log.info("Получаем пользователя по id");
        return userService.findUserById(userId);
    }

    @PostMapping
    public UserDto saveNewUser(@RequestBody @Validated(Create.class) UserDto userDto) {
        log.info("Схраняем нового пользователя");
        return userService.saveUser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable long userId, @RequestBody @Validated(Update.class) UserDto userDto) {
        log.info("Обновляем пользователя {}", userDto.getId());
        return userService.updateUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Удаляем пользователя с id {}", userId);
        userService.deleteUser(userId);
    }
}
