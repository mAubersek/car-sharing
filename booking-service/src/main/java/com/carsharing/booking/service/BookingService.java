package com.carsharing.booking.service;

import com.carsharing.booking.dto.CreateBookingRequest;
import com.carsharing.booking.entity.Booking;
import com.carsharing.booking.event.BookingEventPublisher;
import com.carsharing.booking.integration.VehicleClient;
import com.carsharing.shared.dto.VehicleResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class BookingService {

    @Inject
    JsonWebToken jwt;

    @Inject
    @RestClient
    VehicleClient vehicleClient;

    @Inject
    BookingEventPublisher eventPublisher;

    @Transactional
    public Booking createBooking(CreateBookingRequest req, String authHeader) {
        long userId = Long.parseLong(jwt.getSubject());

        // validate time of booking
        if (!req.getEndTime().isAfter(req.getStartTime())) {
            throw new WebApplicationException(
                    "Booking end time must be after start time",
                    Response.Status.BAD_REQUEST
            );
        }

        long minutes = Duration.between(req.getStartTime(), req.getEndTime()).toMinutes();
        if (minutes < 60) {
            throw new WebApplicationException(
                    "Minimum booking duration is 1 hour",
                    Response.Status.BAD_REQUEST
            );
        }

        // call vehicle client
        VehicleResponse vehicleResponse;
        try {
            vehicleResponse = vehicleClient.getVehicle(req.getVehicleId(), authHeader);
        } catch (WebApplicationException e) {
            if (e.getResponse().getStatus() == 404) {
                throw new WebApplicationException(
                        "Vehicle with id " + req.getVehicleId() + " not found",
                        Response.Status.NOT_FOUND
                );
            }
            throw e; // rethrow
        }

        if (!"AVAILABLE".equals(vehicleResponse.getStatus())) {
            throw new WebApplicationException(
                    "Vehicle is not available for booking",
                    Response.Status.CONFLICT
            );
        }

        // calculate estimated price (per-minute)
        BigDecimal estimatedPrice = vehicleResponse.getPricePerHour()
                .multiply(BigDecimal.valueOf(minutes))
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);

        // persist
        Booking booking = new Booking();
        booking.userId = userId;
        booking.vehicleId = req.getVehicleId();
        booking.startTime = req.getStartTime();
        booking.endTime = req.getEndTime();
        booking.pricePerHour = vehicleResponse.getPricePerHour();
        booking.estimatedPrice = estimatedPrice;
        booking.status = Booking.Status.CONFIRMED;
        booking.persist();

        eventPublisher.publishBookingCreated(booking);

        return booking;
    }

    public Booking getOne(Long id) {
        Booking booking = Booking.findById(id);
        if (booking == null) {
            throw new WebApplicationException(
                    "Booking with id " + id + " not found",
                    Response.Status.NOT_FOUND
            );
        }
        return booking;
    }

    @Transactional
    public Booking startRide(Long id) {
        Booking booking = getOne(id);
        assertOwnership(booking);

        if (booking.status != Booking.Status.CONFIRMED) {
            throw new WebApplicationException(
                    "Booking must have status CONFIRMED to start the ride",
                    Response.Status.CONFLICT
            );
        }
        booking.status = Booking.Status.ACTIVE;
        booking.actualStartTime = LocalDateTime.now();
        return booking;
    }

    @Transactional
    public Booking endRide(Long id) {
        Booking booking = getOne(id);
        assertOwnership(booking);

        if (booking.status != Booking.Status.ACTIVE) {
            throw new WebApplicationException(
                    "Booking must have status ACTIVE to end the ride",
                    Response.Status.CONFLICT);
        }

        booking.status = Booking.Status.COMPLETED;
        booking.actualEndTime = LocalDateTime.now();

        long minutes = Duration.between(booking.actualStartTime, booking.actualEndTime).toMinutes();
        booking.finalPrice = booking.pricePerHour
                .multiply(BigDecimal.valueOf(minutes))
                .divide(BigDecimal.valueOf(60), 2, java.math.RoundingMode.HALF_UP);

        return booking;
    }

    public List<Booking> listByCurrentUserId() {
        long userId = Long.parseLong(jwt.getSubject());
        return Booking.list("userId",  userId);
    }

    @Transactional
    public Booking cancel(Long id) {
        Booking booking = getOne(id);
        assertOwnership(booking);
        if (booking.status == Booking.Status.CANCELED ||
            booking.status == Booking.Status.COMPLETED) {
            throw new WebApplicationException(
                    "Booking is already completed or cancelled",
                    Response.Status.CONFLICT
            );
        }

        booking.status = Booking.Status.CANCELED;

        return booking;
    }

    private void assertOwnership(Booking booking) {
        long callerId = Long.parseLong(jwt.getSubject());
        if (booking.userId != callerId) {
            throw new WebApplicationException(
                    "Booking not created by user",
                    Response.Status.FORBIDDEN
            );
        }
    }

}
