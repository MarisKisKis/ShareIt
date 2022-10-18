package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exeption.EmptyException;
import ru.practicum.exeption.ObjectNotFoundException;
import ru.practicum.exeption.ValidationException;
import ru.practicum.user.UserDto;
import ru.practicum.user.UserRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;

    private final UserRepository userRepository;

    @Override
    public ItemDto getItem(long userId, long itemId) {
        return repository.findItemById(itemId);
    }

    @Override
    public List<ItemDto> getAllItemsByUser(long userId) {
        return repository.getAllItemsByUser(userId);
    }

    @Override
    public ItemDto addNewItem(long userId, ItemDto item) {
        UserDto owner = userRepository.findUserById(userId);
        if (owner == null) {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
        if ((item.getAvailable() == null) || (item.getName() == "") || (item.getDescription() == null)) {
            throw new ValidationException("Необходимо указать статус, имя и описание");
        }
        item.setUserId(userId);
        return repository.save(item);
    }

    @Override
    public List<ItemDto> searchItem (long userId, String text) {
        log.info("Передан запрос на поиск вещи");
        return repository.searchItem(userId, text);
    }

    @Override
    public ItemDto updateItem(ItemDto item, long itemId, long userId) {
        if (userId == 0) {
            throw new ValidationException("Пользователь не найден");
        }

        ItemDto itemCheck = repository.findItemById(itemId);
        UserDto user = userRepository.findUserById(userId);
        if (!user.getId().equals(itemCheck.getUserId())) {
                throw new ObjectNotFoundException("Вещь принадлежит другому пользователю!");
        }

        log.info("Передан запрос на обновление вещи");
        return repository.updateItem(item, itemId);
    }
}
