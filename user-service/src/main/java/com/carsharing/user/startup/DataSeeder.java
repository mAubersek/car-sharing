package com.carsharing.user.startup;


import com.carsharing.user.entity.User;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DataSeeder {

    @Transactional
    void onStart(@Observes StartupEvent ev) {
        if (User.count() > 0) return;

        User admin = new User();
        admin.firstName = "Admin";
        admin.lastName = "Istrator";
        admin.email = "admin@carsharing.com";
        admin.password = BcryptUtil.bcryptHash("admin123");
        admin.role = User.Role.ADMIN;
        admin.persist();

        User user = new User();
        user.firstName = "User";
        user.lastName = "Lastname";
        user.email = "user@exampe.com";
        user.password = BcryptUtil.bcryptHash("user123");
        user.role = User.Role.USER;
        user.persist();
    }
}
