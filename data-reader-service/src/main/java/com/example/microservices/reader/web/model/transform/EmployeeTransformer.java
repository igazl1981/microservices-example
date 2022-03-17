package com.example.microservices.reader.web.model.transform;

import com.example.microservices.reader.web.model.Employee;

import org.springframework.stereotype.Component;

@Component
public class EmployeeTransformer {

    public Employee transform(com.example.microservices.reader.client.model.Employee employee) {
        return new Employee(
                employee.id(),
                employee.firstName(),
                employee.lastName(),
                employee.extension(),
                employee.email(),
                employee.officeCode(),
                employee.reportsTo(),
                employee.jobTitle()
        );
    }
}
