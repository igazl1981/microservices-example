package com.example.microservices.reader.client;

import java.util.List;

import com.example.microservices.reader.client.model.Employee;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

//@FeignClient(value = "client2-client", url = "http://localhost:8082")
@FeignClient(value = "datasource-service")
public interface DataSourceFeignClient {

    @GetMapping("/employees")
    List<Employee> getAllEmployees();
}
