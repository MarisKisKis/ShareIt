package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    private User user1 = new User(1L, "java.ru.practicum.user 1", "user1@email");


    @Test
    void findByOwner() {
        userRepository.save(user1);
        final Optional<User> user = userRepository.findById(user1.getId());
        assertNotNull(user);
    }
}
