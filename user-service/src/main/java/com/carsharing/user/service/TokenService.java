package com.carsharing.user.service;

import com.carsharing.user.entity.User;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;
import java.util.Set;

@ApplicationScoped
public class TokenService {

    public String generateToken(User user) {
        return Jwt.issuer("car-sharing")
                .subject(user.id.toString())
                .upn(user.email)
                .groups(Set.of(user.role.name())) // RolesAllowed checks against
                .claim("firstName", user.firstName) // claim: additional data
                .claim("lastName", user.lastName)
                .expiresIn(Duration.ofHours(24))
                .sign();
    }
}
