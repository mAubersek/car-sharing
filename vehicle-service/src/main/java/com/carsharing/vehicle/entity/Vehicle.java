package com.carsharing.vehicle.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Vehicle extends PanacheEntity {

    @NotBlank
    @Column(nullable = false)
    public String brand;

    @NotBlank
    @Column(nullable = false)
    public String model;

    @NotBlank
    @Column(nullable = false, unique = true)
    public String licensePlate;

    @NotBlank
    @Column(nullable = false)
    public String location;

    @Positive
    @Column(nullable = false)
    public BigDecimal pricePerHour;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public Status status;

    @Column(nullable = false)
    public LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public enum Status {
        AVAILABLE,
        RESERVED,
        IN_USE,
        MAINTENANCE
    }

}
