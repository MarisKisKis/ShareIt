package user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ru.practicum.user.*;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
public class UserServiceTest {

    UserServiceImpl userService;
    UserRepository userRepository;
    private User user;

    @BeforeEach
    void beforeEach() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
        user = new User(1L, "user 1", "user1@email");
    }

    @Test
    void testUpdateUser() {
        UserDto userDto = new UserDto(1L, "user_21", "user21@mail");
        User user = new User(userDto.getId(), userDto.getName(), userDto.getEmail());
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
