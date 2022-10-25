package ru.practicum.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdAndItemId(Long bookerId, Long itemId);

    List<Booking> findAllByBookerId(Long bookerId, Sort sort);

    List<Booking> findByBookerIdAndStartIsAfter(Long bookerId, LocalDateTime start, Sort sort);

    List<Booking> findByBookerIdAndEndIsAfterAndStartIsBefore(Long bookerId, LocalDateTime end,
                                                              LocalDateTime start, Sort sort);

    List<Booking> findByBookerIdAndEndIsBefore(Long bookerId, LocalDateTime end, Sort sort);

    List<Booking> findByBookerIdAndStatus(Long bookerId, BookingStatus status, Sort sort);

    List<Booking> findByItemIdOrderByStartDesc(Long itemId);

    List<Booking> findByItemId_UserId(Long ownerId, Sort sort);

    List<Booking> findByItemId_UserIdAndStartIsAfter(Long ownerId, LocalDateTime start, Sort sort);

    List<Booking> findByItemId_UserIdAndEndIsAfterAndStartIsBefore(Long ownerId, LocalDateTime end,
                                                                   LocalDateTime start, Sort sort);

    List<Booking> findByItemId_UserIdAndEndIsBefore(Long ownerId, LocalDateTime end, Sort sort);

    List<Booking> findByItemId_User_IdAndStatus(Long ownerId, BookingStatus status, Sort sort);
}
