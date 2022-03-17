package com.example.microservices.datasource.service.transformer.employee;

import com.example.microservices.datasource.service.model.Employee;

import org.springframework.stereotype.Service;

@Service
public class EmployeeTransformer {

    public Employee transformEmployee(com.example.microservices.datasource.dao.model.Employee employee) {
        return new Employee(
                employee.getId(),
                employee.getLastName(),
                employee.getFirstName(),
                employee.getExtension(),
                employee.getEmail(),
                employee.getOfficeCode(),
                employee.getReportsTo(),
                employee.getJobTitle()
        );
    }

}
