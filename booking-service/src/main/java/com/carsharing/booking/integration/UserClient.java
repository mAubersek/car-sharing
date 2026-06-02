package com.carsharing.booking.integration;

import com.carsharing.shared.dto.UserResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient
@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
public interface UserClient {

    @GET
    @Path("/{id}")
    UserResponse getUser(@PathParam("id") Long id, @HeaderParam("Authorization") String authHeader);
}
