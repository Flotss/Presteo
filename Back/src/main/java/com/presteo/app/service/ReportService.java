package com.presteo.app.service;

import com.presteo.app.controller.model.CreateReport;
import com.presteo.app.model.Report;
import com.presteo.app.model.User;
import com.presteo.app.repository.ReportsRepository;
import com.presteo.app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReportService {

    private final ReportsRepository reportsRepository;
    private final UserRepository userRepository;

    public ReportService(ReportsRepository reportsRepository, UserRepository userRepository) {
        this.reportsRepository = reportsRepository;
        this.userRepository = userRepository;
    }

    public List<Report> getAllReports() {
        return reportsRepository.findAll();
    }

    public List<Report> getReportByUserId(Long id) {
        return reportsRepository.findByUserReported_Id(id);
    }

    public Report createReport(CreateReport createReport) {
        User reportedUser = validateUserExists(createReport.getUserReportedId());
        User reporterUser = validateUserExists(createReport.getUserReporterId());

        Report report = new Report();
        report.setUserReported(reportedUser);
        report.setUserReporter(reporterUser);
        report.setDescription(createReport.getDescription());

        return reportsRepository.save(report);
    }

    private User validateUserExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));
    }
}
