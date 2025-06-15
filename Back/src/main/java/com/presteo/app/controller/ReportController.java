package com.presteo.app.controller;

import com.presteo.app.controller.model.CreateReport;
import com.presteo.app.dto.ReportDTO;
import com.presteo.app.model.Report;
import com.presteo.app.security.annotation.SecuredRoute;
import com.presteo.app.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsible for managing user reports.
 * Allows creating and viewing reports about inappropriate behavior
 * or issues with other platform users.
 */
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@SecuredRoute
@Tag(name = "Reports", description = "API for managing user reports")
public class ReportController {

    private final ReportService reportService;

    /**
     * Retrieves all reports registered in the system.
     * Accessible only to authenticated users with appropriate permissions.
     *
     * @return List of all reports
     */
    @Operation(summary = "Get all reports", description = "Retrieves the complete list of reports in the system")
    @ApiResponse(responseCode = "200", description = "List of reports successfully retrieved")
    @GetMapping
    public ResponseEntity<List<ReportDTO>> getAllReports() {
        List<ReportDTO> reports = reportService.getAllReports()
                .stream()
                .map(ReportDTO::build)
                .sorted((ReportDTO r1, ReportDTO r2) -> r2.getCreatedAt().compareTo(r1.getCreatedAt()))
                .toList();
        return ResponseEntity.ok(reports);
    }

    /**
     * Retrieves all reports concerning a specific user.
     *
     * @param userId Identifier of the reported user
     * @return List of reports concerning the user
     */
    @Operation(summary = "Get reports about a user", description = "Retrieves the list of reports concerning a specific user")
    @ApiResponse(responseCode = "200", description = "List of user reports successfully retrieved")
    @GetMapping("/{userId}")
    public ResponseEntity<List<ReportDTO>> getReportsByReportedUserId(
            @Parameter(description = "ID of the reported user") 
            @PathVariable Long userId) {
        List<ReportDTO> reportDTOs = reportService.getReportByUserId(userId)
                .stream()
                .map(ReportDTO::build)
                .toList();
        return ResponseEntity.ok(reportDTOs);
    }

    /**
     * Creates a new report in the system.
     *
     * @param report Data for the report to create
     * @return The created report
     */
    @Operation(summary = "Create a report", description = "Registers a new report about a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Report created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid report data")
    })
    @PostMapping("/create")
    public ResponseEntity<ReportDTO> createReport(
            @Parameter(description = "Data for the new report") 
            @RequestBody CreateReport report) {
        Report createdReport = reportService.createReport(report);
        ReportDTO reportDTO = ReportDTO.build(createdReport);
        return ResponseEntity.ok(reportDTO);
    }
}
