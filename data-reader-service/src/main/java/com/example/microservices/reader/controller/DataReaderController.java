package com.example.microservices.reader.controller;

import java.util.List;
import java.util.Optional;

import com.example.microservices.reader.client.DataSourceFeignClient;
import com.example.microservices.reader.client.DataSourceHttpClientService;
import com.example.microservices.reader.client.DatasourceResponse;
import com.example.microservices.reader.controller.model.Employee;
import com.example.microservices.reader.controller.model.transform.EmployeeTransformer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/read/employees")
public class DataReaderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataReaderController.class);

    private final DataSourceHttpClientService dataSourceHttpClientService;
    private final DataSourceFeignClient dataSourceFeignClient;
    private final EmployeeTransformer employeeTransformer;

    public DataReaderController(DataSourceHttpClientService dataSourceHttpClientService, DataSourceFeignClient dataSourceFeignClient, EmployeeTransformer employeeTransformer) {
        this.dataSourceHttpClientService = dataSourceHttpClientService;
        this.dataSourceFeignClient = dataSourceFeignClient;
        this.employeeTransformer = employeeTransformer;
    }

    @GetMapping("/feign")
    public List<Employee> getAllEmployeesByFeign() {
        LOGGER.info("Getting all employees by FeignClient!");
        return dataSourceFeignClient.getAllEmployees()
                .stream().map(employeeTransformer::transform)
                .toList();
    }

    @GetMapping("/with-client-2")
    public SampleResponse callingClient2() {
        LOGGER.info("Sample endpoint called!");

        return Optional.ofNullable(dataSourceHttpClientService.getSampleResponse())
                .map(SampleResponse::fromRemote)
                .orElse(null);
    }

    static class SampleResponse {
        private String message;

        public SampleResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        static SampleResponse fromRemote(DatasourceResponse remote) {
            return new SampleResponse(remote.getMessage());
        }
    }
}
