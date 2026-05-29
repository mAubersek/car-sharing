package com.carsharing.vehicle.api;

import com.carsharing.shared.dto.VehicleResponse;
import com.carsharing.vehicle.dto.CreateVehicleRequest;
import com.carsharing.vehicle.dto.UpdateVehicleRequest;
import com.carsharing.vehicle.entity.Vehicle;
import com.carsharing.vehicle.mapper.VehicleMapper;
import com.carsharing.vehicle.service.VehicleService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/vehicles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VehicleResource {

    @Inject
    VehicleService vehicleService;

    @POST
    @RolesAllowed("ADMIN")
    public Response createVehicle(@Valid CreateVehicleRequest request) {
        Vehicle vehicle = vehicleService.create(request);
        return Response.status(Response.Status.CREATED)
                .entity(VehicleMapper.toResponse(vehicle))
                .build();
    }

    @GET
    @RolesAllowed({"ADMIN", "USER"})
    public List<VehicleResponse> getVehicles() {
        return vehicleService.getAll().stream()
                .map(VehicleMapper::toResponse)
                .toList();
    }

    @GET
    @Path("/available")
    @RolesAllowed({"ADMIN", "USER"})
    public List<VehicleResponse> getAvailableVehicles() {
        return vehicleService.getAvailable().stream()
                .map(VehicleMapper::toResponse)
                .toList();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "USER"})
    public VehicleResponse getVehicle(@PathParam("id") Long id) {
        return VehicleMapper.toResponse(vehicleService.findById(id));
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public VehicleResponse updateVehicle(@PathParam("id") Long id, @Valid UpdateVehicleRequest request) {
        return VehicleMapper.toResponse(vehicleService.update(id, request));
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response deleteVehicle(@PathParam("id") Long id) {
        vehicleService.delete(id);
        return Response.noContent().build();
    }




}
