package com.presteo.app.repository;

import com.presteo.app.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportsRepository extends JpaRepository<Report, Long> {
    List<Report> findByUserReported_Id(Long id);
}
