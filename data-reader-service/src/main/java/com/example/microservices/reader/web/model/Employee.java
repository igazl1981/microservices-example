package com.example.microservices.reader.web.model;

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
