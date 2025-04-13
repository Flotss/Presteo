package com.presteo.app.repository;

import com.presteo.app.model.Role;
import com.presteo.app.model.RoleType;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByName(RoleType name);

    @NotBlank(message = "Role is required")
    Role findByName(RoleType name);
}
