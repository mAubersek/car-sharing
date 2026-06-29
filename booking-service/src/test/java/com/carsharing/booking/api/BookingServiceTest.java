package com.carsharing.booking.api;

import com.carsharing.booking.dto.CreateBookingRequest;
import com.carsharing.booking.integration.VehicleClient;
import com.carsharing.booking.service.BookingService;
import com.carsharing.shared.dto.VehicleResponse;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.jwt.Claim;
import io.quarkus.test.security.jwt.JwtSecurity;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@QuarkusTest
class BookingServiceTest {

    @Inject
    BookingService bookingService;

    @InjectMock
    @RestClient
    VehicleClient vehicleClient;

    @Test
    @TestSecurity(user = "1", roles = "USER")
    @JwtSecurity(claims = { @Claim(key = "sub", value = "1") })
    void cannotBookUnavailableVehicle() {
        VehicleResponse reserved = new VehicleResponse();
        reserved.setId(1L);
        reserved.setStatus("RESERVED");
        reserved.setPricePerHour(BigDecimal.TEN);
        when(vehicleClient.getVehicle(eq(1L), anyString())).thenReturn(reserved);

        CreateBookingRequest req = new CreateBookingRequest();
        req.setVehicleId(1L);
        req.setStartTime(LocalDateTime.now().plusDays(1));
        req.setEndTime(LocalDateTime.now().plusDays(1).plusHours(2));

        WebApplicationException ex = assertThrows(
                WebApplicationException.class,
                () -> bookingService.createBooking(req, "Bearer token")
        );
        assertEquals(409, ex.getResponse().getStatus());
    }
}