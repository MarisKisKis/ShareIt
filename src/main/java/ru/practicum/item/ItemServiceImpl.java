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
import java.util.NoSuchElementException;
import java.util.Optional;

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
        Optional<Item> itemOpt = repository.findById(itemId);
        Item item = itemOpt.get();
        List<Booking> bookings = bookingRepository.findByItemIdOrderByStartDesc(itemId);
        List<Comment> comments = commentRepository.findAllByItemId(itemId);
        List<CommentDto> commentsDto = new ArrayList<>();

        if (!comments.isEmpty() && comments != null) {
            for (Comment comment : comments) {
                commentsDto.add(CommentMapper.toCommentDto(comment));
            }
        }
        if (comments.isEmpty() && comments == null) {
            commentsDto.clear();
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
        List<ItemDto> resultSearch = new ArrayList<>();
        if (text.isEmpty()) {
            return resultSearch;
        }
        List<Item> itemList = repository.search(text);
        for (Item item : itemList) {
            if (item.isAvailable() == true) {
                resultSearch.add(ItemMapper.toItemDto(item));
            }
        }
        return resultSearch;
    }

    @Transactional
    @Override
    public ItemDto updateItem(ItemDto item, long itemId, long userId) {
        Optional <Item> repItem = repository.findById(itemId);
        Item updatedItem = repItem.get();
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
        Optional <User> repUser = userRepository.findById(userId);
        User user = repUser.get();
        if (user.getId() != updatedItem.getUser().getId()) {
                throw new ObjectNotFoundException("Вещь принадлежит другому пользователю!");
        }

        log.info("Передан запрос на обновление вещи");
        return ItemMapper.toItemDto(repository.save(updatedItem));
    }

    @Transactional
    @Override
    public CommentDto addComment(long userId, CommentDto commentDto, long itemId) {
        Optional <User> userOpt = userRepository.findById(userId);
        User user = userOpt.get();
        if (user == null) {
            throw new ValidationException("no user");
        }
        Item item = repository.findById(itemId).orElseThrow(ObjectNotFoundException::new);
        List<Booking> bookings = bookingRepository.findByBookerIdAndItemId(userId, itemId);
        for (Booking booking : bookings) {
            if (booking.getEnd().isBefore(LocalDateTime.now())) {
                Comment comment = CommentMapper.toComment(commentDto, item, user, LocalDateTime.now());
                if (comment.getText().isEmpty()) {
                    throw new ValidationException("no comment text");
                }
                    return CommentMapper.toCommentDto(commentRepository.save(comment));
            }
        }
        throw new ValidationException("no comment");
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
