package com.example.microservices.datasource.service.model;

public record Employee(
        Long id,
        String lastName,
        String firstName,
        String extension,
        String email,
        String officeCode,
        Long reportsTo,
        String jobTitle
) {
}
