package ru.practicum.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings", schema = "public")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bookingId;
    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime start;
    @Column(name = "end_date_time")
    private LocalDateTime end;
    @Column(name = "item_Id", nullable = false)
    private long itemId;
    @Column(name = "booker_Id")
    private long bookerId;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
