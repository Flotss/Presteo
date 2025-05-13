package com.presteo.app.repository;

import com.presteo.app.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByCustomer_Id(Long customerId);

    List<Booking> findAllByService_Provider_Id(Long serviceProviderId);
}
