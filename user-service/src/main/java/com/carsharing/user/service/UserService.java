package com.carsharing.user.service;


import com.carsharing.user.dto.LoginRequest;
import com.carsharing.user.dto.RegisterRequest;
import com.carsharing.user.entity.User;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class UserService {

    @Transactional
    public User register(RegisterRequest req) {
        PanacheQuery<User> query = User.find("email", req.getEmail());
        if (query.firstResult() != null) {
            throw new WebApplicationException(
                    "Email already registered",
                    Response.Status.CONFLICT
            );
        }

        User user = new User();
        user.firstName = req.getFirstName();
        user.lastName = req.getLastName();
        user.email = req.getEmail();
        user.password = BcryptUtil.bcryptHash(req.getPassword());
        user.role = User.Role.USER;
        user.persist();

        return user;
    }

    public User login(LoginRequest req) {
        User user = User.find("email", req.getEmail()).firstResult();

        if (user == null || !BcryptUtil.matches(req.getPassword(), user.password)) {
            throw new WebApplicationException(
                    "Invalid credentials", Response.Status.UNAUTHORIZED);
        }

        return user;
    }

    public User findById(Long id) {
        User user = User.findById(id);
        if (user == null) {
            throw new WebApplicationException(
                    "User not found", Response.Status.NOT_FOUND);
        }

        return user;
    }
}
