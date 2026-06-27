package com.carsharing.booking.mapper;

import com.carsharing.booking.entity.Booking;
import com.carsharing.shared.dto.BookingResponse;

public class BookingMapper {

    public static BookingResponse toResponse(Booking booking) {
        BookingResponse res = new BookingResponse();
        res.setId(booking.id);
        res.setUserId(booking.userId);
        res.setVehicleId(booking.vehicleId);
        res.setStartTime(booking.startTime);
        res.setEndTime(booking.endTime);
        res.setEstimatedPrice(booking.estimatedPrice);
        res.setFinalPrice(booking.finalPrice);
        res.setActualStartTime(booking.actualStartTime);
        res.setActualEndTime(booking.actualEndTime);
        res.setStatus(booking.status.toString());
        res.setCreatedAt(booking.createdAt);
        return res;
    }
}
