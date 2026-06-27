package com.carsharing.booking.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking extends PanacheEntity {

    @NotNull
    @Column(nullable = false)
    public Long userId;

    @NotNull
    @Column(nullable = false)
    public Long vehicleId;

    @NotNull
    @Column(nullable = false)
    public LocalDateTime startTime;

    @NotNull
    @Column(nullable = false)
    public LocalDateTime endTime;

    @Column(nullable = false)
    public BigDecimal pricePerHour;

    @Column(nullable = false)
    public BigDecimal estimatedPrice;

    public BigDecimal finalPrice;

    public LocalDateTime actualStartTime;

    public LocalDateTime actualEndTime;

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
        CONFIRMED,  // not yet started
        ACTIVE,     // user picked up the car
        COMPLETED,  // car returned
        CANCELED    // canceled before start
    }
}
