package com.carsharing.user.api;


import com.carsharing.user.dto.AuthResponse;
import com.carsharing.user.dto.LoginRequest;
import com.carsharing.user.dto.RegisterRequest;
import com.carsharing.shared.dto.UserResponse;
import com.carsharing.user.entity.User;
import com.carsharing.user.mapper.UserMapper;
import com.carsharing.user.service.TokenService;
import com.carsharing.user.service.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;

    @Inject
    TokenService tokenService;

    @POST
    @Path("/auth/register")
    @PermitAll
    public Response register(@Valid RegisterRequest req) {
        User user = userService.register(req);
        String token = tokenService.generateToken(user);
        return Response.status(Response.Status.CREATED)
                .entity(new AuthResponse(token, user.email, user.role.name()))
                .build();
    }

    @POST
    @Path("auth/login")
    @PermitAll
    public Response login(@Valid LoginRequest req) {
        User user = userService.login(req);
        String token = tokenService.generateToken(user);
        return Response.ok(
                new AuthResponse(token, user.email, user.role.name())
        ).build();
    }

    @GET
    @Path("users/{id}")
    @RolesAllowed({"USER", "ADMIN"})
    public UserResponse getUser(@PathParam("id") Long id) {
        User user = userService.findById(id);
        return UserMapper.toResponse(user);
    }
}
