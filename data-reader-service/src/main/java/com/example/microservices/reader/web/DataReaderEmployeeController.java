package com.example.microservices.reader.web;

import java.util.List;

import com.example.microservices.reader.client.DataSourceFeignClient;
import com.example.microservices.reader.client.DataSourceHttpClientService;
import com.example.microservices.reader.web.model.Employee;
import com.example.microservices.reader.web.model.transform.EmployeeTransformer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/read/employees")
public class DataReaderEmployeeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataReaderEmployeeController.class);

    private final DataSourceHttpClientService dataSourceHttpClientService;
    private final DataSourceFeignClient dataSourceFeignClient;
    private final EmployeeTransformer employeeTransformer;

    public DataReaderEmployeeController(DataSourceHttpClientService dataSourceHttpClientService, DataSourceFeignClient dataSourceFeignClient, EmployeeTransformer employeeTransformer) {
        this.dataSourceHttpClientService = dataSourceHttpClientService;
        this.dataSourceFeignClient = dataSourceFeignClient;
        this.employeeTransformer = employeeTransformer;
    }

    @GetMapping("/feign")
    public List<Employee> getAllEmployeesByFeign() {
        LOGGER.info("Getting all employees by FeignClient!");
        return transformEmployees(dataSourceFeignClient.getAllEmployees());
    }

    @GetMapping("/http-client")
    public List<Employee> getAllEmployeesByHttpClient() {
        LOGGER.info("Sample endpoint called!");
        return dataSourceHttpClientService.getAllEmployees()
                .map(this::transformEmployees)
                .orElse(null);
    }

    private List<Employee> transformEmployees(List<com.example.microservices.reader.client.model.Employee> employees) {
        return employees.stream().map(employeeTransformer::transform).toList();
    }

}
