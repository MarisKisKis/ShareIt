package user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
class UserRepositoryTest {

    UserRepository userRepository;

    User user1;

    User user2;


    @BeforeEach
    void beforeEach() {
        user1 = userRepository.save(new User(1L, "user 1", "user1@email"));
    }

    @Test
    void findByOwner() {
        final Optional<User> user = userRepository.findById(user1.getId());
        assertNotNull(user);
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }
}
