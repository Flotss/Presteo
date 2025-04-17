package com.presteo.app.controller;

import com.presteo.app.controller.model.CreateReport;
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
    public ResponseEntity<List<Report>> getReportsByReportedUserId(@PathVariable Long userId) {
        List<Report> reports = reportService.getReportByUserId(userId);
        return ResponseEntity.ok(reports);
    }

    @PostMapping("/create")
    public ResponseEntity<Report> createReport(@RequestBody CreateReport report) {
        Report createdReport = reportService.createReport(report);
        return ResponseEntity.ok(createdReport);
    }
}
