package item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import ru.practicum.item.Item;
import ru.practicum.item.ItemRepository;
import ru.practicum.request.ItemRequest;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
class ItemRepositoryTest {

    UserRepository userRepository;

    ItemRepository itemRepository;

    User user1;
    Item item1;
    User user2;
    Item item2;
    ItemRequest request1;
    ItemRequest request2;

    @BeforeEach
    void beforeEach() {
        user1 = userRepository.save(new User(1L, "user 1", "user1@email"));

        item1 = itemRepository.save(new Item(1L, "item 1", "item 1 description", true, user1, request1));

        user2 = userRepository.save(new User(2L, "user 2", "user2@email"));

        item2 = itemRepository.save(new Item(2L, "item 2", "item 2 description", true, user2, request2));
    }

    @Test
    void findByOwner() {
        final Page<Item> byOwner = (Page<Item>) itemRepository.findAllByUserIdOrderById(user1.getId(), Pageable.unpaged());
        assertNotNull(byOwner);
        assertEquals(1, byOwner.getTotalElements());
    }

    @AfterEach
    void afterEach() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }
}
