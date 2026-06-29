package com.carsharing.user.api;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
public class UserResourceTest {

    @Test
    void registerReturnsToken() {
        String email = "test_" + UUID.randomUUID() + "@example.com";

        given()
                .contentType(ContentType.JSON)
                .body("""
                    {
                    "firstName": "Test",
                    "lastName": "User",
                    "email": "%s",
                    "password": "secret123"
                    }
                    """.formatted(email))
                .when()
                    .post("/api/auth/register")
                .then()
                    .statusCode(201)
                    .body("token", notNullValue())
                    .body("email", Matchers.equalTo(email))
                    .body("role", Matchers.equalTo("USER"));


    }

    @Test
    void loginWithBadPasswordReturns401() {
        // seeded by DataSeeder
        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                  "email": "admin@carsharing.com",
                  "password": "wrong-password"
                }
                """)
                .when()
                    .post("/api/auth/login")
                .then()
                    .statusCode(401);
    }
}
