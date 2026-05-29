package com.carsharing.vehicle.service;

import com.carsharing.vehicle.dto.CreateVehicleRequest;
import com.carsharing.vehicle.dto.UpdateVehicleRequest;
import com.carsharing.vehicle.entity.Vehicle;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.util.List;

@ApplicationScoped
public class VehicleService {

    @Transactional
    public Vehicle create(CreateVehicleRequest request) {
        if (Vehicle.find("licensePlate", request.getLicensePlate()).firstResult() != null) {
            throw new WebApplicationException(
                    "Vehicle with licence plate " +  request.getLicensePlate() + " already exists.",
                    Response.Status.CONFLICT
            );
        }

        Vehicle vehicle = new Vehicle();
        vehicle.brand = request.getBrand();
        vehicle.model = request.getModel();
        vehicle.licensePlate = request.getLicensePlate();
        vehicle.location = request.getLocation();
        vehicle.pricePerHour = request.getPricePerHour();
        vehicle.status = Vehicle.Status.AVAILABLE;
        vehicle.persist();

        return vehicle;
    }

    public List<Vehicle>  getAll() {
        return Vehicle.listAll();
    }

    public List<Vehicle> getAvailable() {
        return Vehicle.list("status", Vehicle.Status.AVAILABLE);
    }

    public Vehicle findById(Long id) {
        Vehicle vehicle = Vehicle.findById(id);
        if (vehicle == null) {
            throw new WebApplicationException(
                    "Vehicle with id " + id + " not found.", Response.Status.NOT_FOUND);
        }
        return vehicle;
    }

    @Transactional
    public Vehicle update(Long id, UpdateVehicleRequest req) {
        // findById handles not found
        Vehicle vehicle = findById(id);

        if (req.getBrand() != null)
            vehicle.brand = req.getBrand();
        if (req.getModel() != null)
            vehicle.model = req.getModel();
        if (req.getLicensePlate() != null)
            vehicle.licensePlate = req.getLicensePlate();
        if (req.getLocation() != null)
            vehicle.location = req.getLocation();
        if (req.getPricePerHour() != null)
            vehicle.pricePerHour = req.getPricePerHour();
        if (req.getStatus() != null)
            vehicle.status = req.getStatus();

        return vehicle;
    }

    @Transactional
    public Vehicle updateStatus(Long id, Vehicle.Status status) {
        // findById handles not found
        Vehicle vehicle = findById(id);
        vehicle.status = status;
        return vehicle;
    }

    @Transactional
    public void delete(Long id) {
        Vehicle vehicle = findById(id);
        vehicle.delete();
    }
}
