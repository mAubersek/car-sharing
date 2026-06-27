package com.carsharing.booking.api;

import com.carsharing.booking.dto.CreateBookingRequest;
import com.carsharing.booking.entity.Booking;
import com.carsharing.booking.mapper.BookingMapper;
import com.carsharing.booking.service.BookingService;
import com.carsharing.shared.dto.BookingResponse;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/bookings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookingResource {

    @Inject
    BookingService bookingService;

    @POST
    @RolesAllowed({"USER", "ADMIN"})
    public Response createBooking(@Valid CreateBookingRequest req, @HeaderParam("Authorization") String authHeader) {
        Booking booking = bookingService.createBooking(req, authHeader);
        return Response.status(Response.Status.CREATED)
                .entity(BookingMapper.toResponse(booking))
                .build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"USER", "ADMIN"})
    public BookingResponse getBooking(@PathParam("id") Long id) {
        return BookingMapper.toResponse(bookingService.getOne(id));
    }

    @GET
    @Path("/my")
    @RolesAllowed({"USER", "ADMIN"})
    public List<BookingResponse> getCurrentUserBookings() {
        return bookingService.listByCurrentUserId().stream()
                .map(BookingMapper::toResponse)
                .toList();
    }

    @POST
    @Path("/{id}/cancel")
    @RolesAllowed({"USER", "ADMIN"})
    public BookingResponse cancelBooking(@PathParam("id") Long id) {
        return BookingMapper.toResponse(bookingService.cancel(id));
    }

    @POST
    @Path("/{id}/start")
    @RolesAllowed({"USER", "ADMIN"})
    public BookingResponse startBooking(@PathParam("id") Long id) {
        return BookingMapper.toResponse(bookingService.startRide(id));
    }

    @POST
    @Path("/{id}/end")
    @RolesAllowed({"USER", "ADMIN"})
    public BookingResponse endBooking(@PathParam("id") Long id) {
        return BookingMapper.toResponse(bookingService.endRide(id));
    }
}
