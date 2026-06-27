package com.carsharing.booking.event;

import com.carsharing.booking.entity.Booking;
import com.carsharing.shared.event.BookingCreatedEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class BookingEventPublisher {
    @Inject
    @Channel("booking-created")
    Emitter<BookingCreatedEvent> bookingCreatedEmmiter;

    public void publishBookingCreated(Booking booking) {
        BookingCreatedEvent event = new BookingCreatedEvent(
                booking.id,
                booking.userId,
                booking.vehicleId,
                booking.startTime,
                booking.endTime,
                booking.estimatedPrice
        );
        bookingCreatedEmmiter.send(event);
    }
}
