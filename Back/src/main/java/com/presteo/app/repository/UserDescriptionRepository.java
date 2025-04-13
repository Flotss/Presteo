package com.presteo.app.repository;

import com.presteo.app.model.User;
import com.presteo.app.model.UserDescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDescriptionRepository extends JpaRepository<UserDescription, Long> {
    Optional<UserDescription> findByUser(User user);
}
