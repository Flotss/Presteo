package com.presteo.app.controller.model;

import lombok.Data;

@Data
public class ReviewForm {
    private Long serviceId;
    private Long customerId;
    private Integer rating;
    private String reviewText;
} 