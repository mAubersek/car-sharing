package com.carsharing.vehicle.dto;

import com.carsharing.vehicle.entity.Vehicle;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Setter
@Getter
public class UpdateVehicleRequest {
    private String brand;
    private String model;
    private String licensePlate;
    private String location;
    private BigDecimal pricePerHour;
    private Vehicle.Status status;
}
