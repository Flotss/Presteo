package com.presteo.app.repository;

import com.presteo.app.model.ProviderInformation;
import com.presteo.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProviderInformationRepository extends JpaRepository<ProviderInformation, Long> {
    Optional<ProviderInformation> findByUser(User user);
}
