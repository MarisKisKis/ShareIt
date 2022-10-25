package ru.practicum.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.booking.Booking;
import ru.practicum.booking.BookingRepository;
import ru.practicum.exeption.ObjectNotFoundException;
import ru.practicum.exeption.ValidationException;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;


@Service
@Slf4j
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    private final BookingRepository bookingRepository;

    @Autowired
    public ItemServiceImpl (ItemRepository repository, UserRepository userRepository, CommentRepository commentRepository,
                            BookingRepository bookingRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public ItemInfoDto getItem(long userId, long itemId) {
        if (itemId == 0) {
            throw new ObjectNotFoundException("Нет такой вещи");
        }
        Item item = repository.findById(itemId).orElseThrow(ObjectNotFoundException::new);
        List<Booking> bookings = bookingRepository.findByItemIdOrderByStartDesc(itemId);
        List<Comment> comments = commentRepository.findAllByItemId(itemId);
        List<CommentDto> commentsDto = new ArrayList<>();

        if (!comments.isEmpty() && comments != null) {
            for (Comment comment : comments) {
                commentsDto.add(CommentMapper.toCommentDto(comment));
            }
        }
        if (bookings.isEmpty() || (item.getUser().getId() != userId)) {
            return ItemMapper.toItemInfoDto(item, null, null, commentsDto);
        }
        Booking lastBooking = findLastBooking(bookings);
        Booking nextBooking = findNextBooking(bookings);
        return ItemMapper.toItemInfoDto(item, lastBooking, nextBooking, commentsDto);
    }

    @Override
    public List<ItemInfoDto> getAllItemsByUser(long userId) {
        if (userId == 0) {
            throw new ValidationException("Нет такого пользователя");
        }
        User user = userRepository.findById(userId).orElseThrow(ObjectNotFoundException::new);
        List<Item> items = repository.findAllByUserIdOrderById(userId);
        List<ItemInfoDto> itemsByUser = new ArrayList<>();
        for (Item item : items) {
            List<Booking> bookings = bookingRepository.findByItemIdOrderByStartDesc(item.getId());
            Booking lastBooking = findLastBooking(bookings);
            Booking nextBooking = findNextBooking(bookings);
            List<CommentDto> comments = commentRepository.findAllByItemId(item.getId())
                    .stream()
                    .map(CommentMapper::toCommentDto)
                    .collect(toList());
            itemsByUser.add(ItemMapper.toItemInfoDto(item, lastBooking, nextBooking, comments));
        }
        return itemsByUser;
    }

    @Transactional
    @Override
    public ItemDto addNewItem(long userId, ItemDto itemDto) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден"));
        if ((itemDto.getAvailable() == null) || (itemDto.getName() == "") || (itemDto.getDescription() == null)) {
            throw new ValidationException("Необходимо указать статус, имя и описание");
        }
        Item item = ItemMapper.toItem(itemDto, owner);
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
        if (user.getId() != updatedItem.getUser().getId()) {
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

    private Booking findLastBooking(List<Booking> bookings) {
        Booking lastBooking = null;
        for (Booking booking : bookings) {
            if (booking.getEnd().isBefore(LocalDateTime.now())) {
                lastBooking = booking;
            }
        }
        return lastBooking;
    }

    private Booking findNextBooking(List<Booking> bookings) {
        Booking nextBooking = null;
        for (Booking booking : bookings) {
            if (booking.getStart().isAfter(LocalDateTime.now())) {
                nextBooking = booking;
            }
        }
        return nextBooking;
    }
}
