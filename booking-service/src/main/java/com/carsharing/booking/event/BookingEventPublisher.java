package com.carsharing.booking.event;

import com.carsharing.booking.entity.Booking;
import com.carsharing.shared.event.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class BookingEventPublisher {

    @Inject
    @Channel("booking-created")
    Emitter<BookingCreatedEvent> bookingCreatedEmmiter;

    @Inject
    @Channel("booking-started")
    Emitter<BookingStartedEvent> bookingStartedEventEmitter;

    @Inject
    @Channel("booking-ended")
    Emitter<BookingEndedEvent> bookingEndedEventEmitter;

    @Inject
    @Channel("booking-cancelled")
    Emitter<BookingCancelledEvent> bookingCancelledEventEmitter;


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

    public void publishBookingStarted(Booking booking) {
        BookingStartedEvent event = new BookingStartedEvent(
                booking.id,
                booking.vehicleId
        );
        bookingStartedEventEmitter.send(event);
    }

    public void publishBookingEnded(Booking booking) {
        BookingEndedEvent event = new BookingEndedEvent(
                booking.id,
                booking.vehicleId
        );
        bookingEndedEventEmitter.send(event);
    }

    public void publishBookingCancelled(Booking booking) {
        BookingCancelledEvent event = new BookingCancelledEvent(
                booking.id,
                booking.vehicleId
        );
        bookingCancelledEventEmitter.send(event);
    }
}
