package ru.practicum.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.booking.BookingRepository;
import ru.practicum.exeption.ObjectNotFoundException;
import ru.practicum.exeption.ValidationException;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;


@Service
@Slf4j
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;

    private final UserRepository userRepository;

    private CommentRepository commentRepository;

    private BookingRepository bookingRepository;

    @Autowired
    public ItemServiceImpl (ItemRepository repository, UserRepository userRepository, CommentRepository commentRepository,
                            BookingRepository bookingRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public ItemDto getItem(long userId, long itemId) {
        return ItemMapper.toItemDto(repository.getById(itemId));
    }

    @Override
    public List<ItemDto> getAllItemsByUser(long userId) {
        return repository.findAllByOwnerIdOrderById(userId)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(toList());
    }

    @Transactional
    @Override
    public ItemDto addNewItem(long userId, ItemDto itemDto) {
        User owner = userRepository.getById(userId);
        if (owner == null) {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
        if ((itemDto.getAvailable() == null) || (itemDto.getName() == "") || (itemDto.getDescription() == null)) {
            throw new ValidationException("Необходимо указать статус, имя и описание");
        }
        Item item = ItemMapper.toItem(itemDto);
        item.setUserId(userId);
        return ItemMapper.toItemDto(repository.save(item));
    }

    @Override
    public List<ItemDto> searchItem (long userId, String text) {
        log.info("Передан запрос на поиск вещи");
        return repository.search(text)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(toList());
    }

    @Transactional
    @Override
    public ItemDto updateItem(ItemDto item, long itemId, long userId) {
        Item updatedItem = repository.getById(itemId);
        if (item.getAvailable() != null) {
            updatedItem.setAvailable(item.getAvailable());
        }
        if (item.getName() != null) {
            updatedItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updatedItem.setDescription(item.getDescription());
        }
        if (userId == 0) {
            throw new ValidationException("Пользователь не найден");
        }
        User user = userRepository.getById(userId);
        if (!user.getId().equals(updatedItem.getUserId())) {
                throw new ObjectNotFoundException("Вещь принадлежит другому пользователю!");
        }

        log.info("Передан запрос на обновление вещи");
        return ItemMapper.toItemDto(repository.save(updatedItem));
    }

    @Transactional
    @Override
    public CommentDto addComment(long userId, CommentDto commentDto, long itemId) {
        Comment comment = CommentMapper.toComment(commentDto);
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }
}
