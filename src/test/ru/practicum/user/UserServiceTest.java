package ru.practicum.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.user.*;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes= UserService.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    private UserServiceImpl userService;
    private User user;


    @BeforeEach
    void beforeEach() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
        user = new User(1L, "java.ru.practicum.user 1", "user1@email");
    }

    @Test
    void testUpdateUser() {
        UserDto userDto = UserMapper.toUserDto(user);
        when(userRepository.save(user))
                .thenReturn(user);
        when(userRepository.findById(userDto.getId()))
                .thenReturn(Optional.ofNullable(user));
        UserDto userDto1 = userService.updateUser(userDto.getId(), userDto);

        assertNotNull(userDto1);
        assertEquals(user.getId(), userDto1.getId());
        assertEquals(user.getName(), userDto1.getName());
    }
}
