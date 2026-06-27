package com.carsharing.vehicle.event;

import com.carsharing.shared.event.BookingCancelledEvent;
import com.carsharing.shared.event.BookingCreatedEvent;
import com.carsharing.shared.event.BookingEndedEvent;
import com.carsharing.shared.event.BookingStartedEvent;
import com.carsharing.vehicle.entity.Vehicle;
import com.carsharing.vehicle.service.VehicleService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

@ApplicationScoped
public class BookingEventListener {
    private static final Logger LOGGER = Logger.getLogger(BookingEventListener.class.getName());

    @Inject
    VehicleService vehicleService;

    @Incoming("booking-created")
    public void bookingCreated(BookingCreatedEvent event) {
        LOGGER.infof("Received booking.created event: bookingId=%d, vehicleId=%d",
                event.getBookingId(), event.getVehicleId());
        try {
            vehicleService.updateStatus(event.getVehicleId(), Vehicle.Status.RESERVED);
        } catch (Exception e) {
            LOGGER.errorf(e, "Failed to update vehicle %d status", event.getVehicleId());
        }
    }

    @Incoming("booking-started")
    public void bookingStarted(BookingStartedEvent event) {
        LOGGER.infof("Recieved booking.started event: bookingId=%d vehicleId=%d",
                event.getBookingId(), event.getVehicleId());
        try {
            vehicleService.updateStatus(event.getVehicleId(), Vehicle.Status.IN_USE);
        } catch (Exception e) {
            LOGGER.errorf(e, "Failed to update vehicle %d status", event.getVehicleId());
        }
    }

    @Incoming("booking-ended")
    public void bookingEnded(BookingEndedEvent event) {
        LOGGER.infof("Recieved booking.ended event: bookingId=%d vehicleId=%d",
                event.getBookingId(), event.getVehicleId());
        try {
            vehicleService.updateStatus(event.getVehicleId(), Vehicle.Status.AVAILABLE);
        } catch (Exception e) {
            LOGGER.errorf(e, "Failed to update vehicle %d status", event.getVehicleId());
        }
    }

    @Incoming("booking-cancelled")
    public void bookingCancelled(BookingCancelledEvent event) {
        LOGGER.infof("Recieved booking.cancelled event: bookingId=%d vehicleId=%d",
                event.getBookingId(), event.getVehicleId());
        try {
            vehicleService.updateStatus(event.getVehicleId(), Vehicle.Status.AVAILABLE);
        } catch (Exception e) {
            LOGGER.errorf(e, "Failed to update vehicle %d status", event.getVehicleId());
        }
    }
}
