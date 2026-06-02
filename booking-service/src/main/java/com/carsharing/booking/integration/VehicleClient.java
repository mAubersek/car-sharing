package com.carsharing.booking.integration;

import com.carsharing.shared.dto.VehicleResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "vehicle-service") // connects interface to the url configured in .properties
@Path("/api/vehicles")
@Produces(MediaType.APPLICATION_JSON)
public interface VehicleClient {

    /*
    * @HeaderParam("Authorization") - booking-service forwards the JWT, since the endpoints are protected there
    * interface because Quarkus generates the HTTP code at build
    */
    @GET
    @Path("/{id}")
    VehicleResponse getVehicle(@PathParam("id") Long id, @HeaderParam("Authorization") String authHeader);
}