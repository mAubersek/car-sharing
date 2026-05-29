package com.carsharing.vehicle.mapper;

import com.carsharing.shared.dto.VehicleResponse;
import com.carsharing.vehicle.entity.Vehicle;

public class VehicleMapper {

    public static VehicleResponse toResponse(Vehicle vehicle) {
        VehicleResponse response = new VehicleResponse();
        response.setId(vehicle.id);
        response.setBrand(vehicle.brand);
        response.setModel(vehicle.model);
        response.setLicensePlate(vehicle.licensePlate);
        response.setLocation(vehicle.location);
        response.setPricePerHour(vehicle.pricePerHour);
        response.setStatus(vehicle.status.toString());
        response.setCreatedAt(vehicle.createdAt);
        return response;
    }
}
