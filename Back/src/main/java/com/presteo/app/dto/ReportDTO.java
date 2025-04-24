package com.presteo.app.dto;


import com.presteo.app.model.Report;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    private Long id;
    private UserDTO userReported;
    private UserDTO userReporter;
    private String description;
    private String createdAt;
    private String updatedAt;

    public static ReportDTO build(Report report) {
        return builder()
                .id(report.getId())
                .userReported(UserDTO.build(report.getUserReported()))
                .userReporter(UserDTO.build(report.getUserReporter()))
                .description(report.getDescription())
                .createdAt(report.getCreatedAt().toString())
                .updatedAt(report.getUpdatedAt().toString())
                .build();
    }
}
