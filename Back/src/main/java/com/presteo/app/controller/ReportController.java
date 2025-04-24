package com.presteo.app.controller;

import com.presteo.app.controller.model.CreateReport;
import com.presteo.app.dto.ReportDTO;
import com.presteo.app.model.Report;
import com.presteo.app.security.annotation.SecuredRoute;
import com.presteo.app.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@SecuredRoute
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        List<Report> reports = reportService.getAllReports();
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ReportDTO>> getReportsByReportedUserId(@PathVariable Long userId) {
        List<ReportDTO> reportDTOs = reportService.getReportByUserId(userId)
                .stream()
                .map(ReportDTO::build)
                .toList();
        return ResponseEntity.ok(reportDTOs);
    }

    @PostMapping("/create")
    public ResponseEntity<Report> createReport(@RequestBody CreateReport report) {
        Report createdReport = reportService.createReport(report);
        return ResponseEntity.ok(createdReport);
    }
}
