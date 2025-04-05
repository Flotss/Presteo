package com.presteo.app.repository;

import com.presteo.app.model.RoleType;
import com.presteo.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    boolean existsByUsername(String username);

    List<User> findByRole_Name(RoleType roleType);

    Optional<User> findByEmail(String email);
}