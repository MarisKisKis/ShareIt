package ru.practicum.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exeption.ObjectNotFoundException;
import ru.practicum.exeption.ValidationException;
import ru.practicum.item.Item;
import ru.practicum.item.ItemRepository;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    @Autowired
    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemRequestDto createRequest(long userId, ItemRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Нет такого пользователя"));
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(requestDto, user);
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<RequestAndResponseDto> findOwnerRequests(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Нет такого пользователя"));
        Sort createdDesc = Sort.by(Sort.Direction.DESC, "created");
        List<RequestAndResponseDto> resultRequestAndResponse = new ArrayList<>();
        List<ItemRequest> requestList = itemRequestRepository.findByRequesterId(userId, createdDesc);
        for (ItemRequest request : requestList) {
            List<Item> itemList = itemRepository.findByRequest_RequestId(request.getRequestId());
            resultRequestAndResponse.add(ItemRequestMapper.toRequestAndResponseDto(request, itemList));
        }
        return resultRequestAndResponse;
    }

    @Override
    public List<RequestAndResponseDto> findUserRequests (long userId, Integer from, Integer size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Нет такого пользователя"));
        List<RequestAndResponseDto> resultRequestAndResponse = new ArrayList<>();
        if (size == null && from == null) {
            return resultRequestAndResponse;
        }
        if (from < 0) {
            throw new ValidationException("from меньше 0");
        }
        if (size <= 0) {
            throw new ValidationException("size меньше либо равно 0");
        }
        Sort createdDesc = Sort.by(Sort.Direction.DESC, "created");
        Pageable pageable = PageRequest.of(from / size, size, createdDesc);
        Page<ItemRequest> requestList = itemRequestRepository.findAll(pageable);
        for (ItemRequest request : requestList) {
            if (request.getRequester().getId() != userId) {
                List<Item> itemList = itemRepository.findByRequest_RequestId(request.getRequestId());
                resultRequestAndResponse.add(ItemRequestMapper.toRequestAndResponseDto(request, itemList));
            }
        }
        return resultRequestAndResponse;
    }

    @Override
    public RequestAndResponseDto getRequest(long userId, long id) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Нет такого пользователя"));
        ItemRequest itemRequest = itemRequestRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Ничего не найдено"));
        List<Item> itemList = itemRepository.findByRequest_RequestId(itemRequest.getRequestId());
        return ItemRequestMapper.toRequestAndResponseDto(itemRequest, itemList);
    }
}
