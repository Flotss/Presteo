package com.presteo.app.repository;

import com.presteo.app.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findAllByProvider_Id(Long providerId);
}
