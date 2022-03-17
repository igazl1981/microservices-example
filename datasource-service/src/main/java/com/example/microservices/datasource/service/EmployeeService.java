package com.example.microservices.datasource.service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.microservices.datasource.dao.repository.EmployeeRepository;
import com.example.microservices.datasource.service.model.Employee;
import com.example.microservices.datasource.service.transformer.employee.EmployeeTransformer;

import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeTransformer employeeTransformer;

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeTransformer employeeTransformer) {
        this.employeeRepository = employeeRepository;
        this.employeeTransformer = employeeTransformer;
    }

    public List<Employee> getAll() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeTransformer::transformEmployee)
                .collect(Collectors.toList());
    }
}
