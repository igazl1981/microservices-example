package com.example.microservices.datasource.dao.repository;

import com.example.microservices.datasource.dao.model.Employee;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
