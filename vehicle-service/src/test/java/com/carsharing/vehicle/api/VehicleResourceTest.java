package com.carsharing.vehicle.api;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class VehicleResourceTest {

    @Test
    @TestSecurity(user = "user@example.com", roles = "USER")
    void userCannotCreateVehicle() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                  "brand": "Test",
                  "model": "X",
                  "licensePlate": "TEST-1",
                  "location": "Anywhere",
                  "pricePerHour": 5.00
                }
                """)
        .when()
            .post("/api/vehicles")
        .then()
            .statusCode(403);
    }

    @Test
    @TestSecurity(user = "admin@example.com", roles = "ADMIN")
    void adminCanListVehicles() {
        given()
        .when()
            .get("/api/vehicles")
        .then()
            .statusCode(200);
    }
}