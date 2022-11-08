package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdAndItemId(Long bookerId, Long itemId);

    List<Booking> findAllByBookerId(Long bookerId, Pageable pageable);

    List<Booking> findByBookerIdAndStartIsAfter(Long bookerId, LocalDateTime start, Pageable pageable);

    List<Booking> findByBookerIdAndEndIsAfterAndStartIsBefore(Long bookerId, LocalDateTime end,
                                                              LocalDateTime start, Pageable pageable);

    List<Booking> findByBookerIdAndEndIsBefore(Long bookerId, LocalDateTime end, Pageable pageable);

    List<Booking> findByBookerIdAndStatus(Long bookerId, BookingStatus status, Pageable pageable);

    List<Booking> findByItemIdOrderByStartDesc(Long itemId);

    List<Booking> findByItemId_UserId(Long ownerId, Pageable pageable);

    List<Booking> findByItemId_UserIdAndStartIsAfter(Long ownerId, LocalDateTime start, Pageable pageable);

    List<Booking> findByItemId_UserIdAndEndIsAfterAndStartIsBefore(Long ownerId, LocalDateTime end,
                                                                   LocalDateTime start, Pageable pageable);

    List<Booking> findByItemId_UserIdAndEndIsBefore(Long ownerId, LocalDateTime end, Pageable pageable);

    List<Booking> findByItemId_User_IdAndStatus(Long ownerId, BookingStatus status, Pageable pageable);
}
