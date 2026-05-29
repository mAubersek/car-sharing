package com.carsharing.shared.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class VehicleResponse {
    private Long id;
    private String brand;
    private String model;
    private String licensePlate;
    private String location;
    private BigDecimal pricePerHour;
    private String status;
    private LocalDateTime createdAt;
}
