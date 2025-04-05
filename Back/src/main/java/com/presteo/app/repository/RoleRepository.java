package com.presteo.app.repository;

import com.presteo.app.model.Role;
import com.presteo.app.model.RoleType;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByName(RoleType name);

    @NotBlank(message = "Role is required")
    Role findByName(RoleType name);

    // Nouvelle méthode pour chercher un rôle à partir d'un String
    @Query("SELECT r FROM Role r WHERE r.name = :name")
    Optional<Role> findByNameString(@Param("name") String name);
}
