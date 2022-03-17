package com.example.microservices.reader.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.example.microservices.reader.client.model.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.TraceContext;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Service;

@Service
public class DataSourceHttpClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceHttpClientService.class);

    private final ObjectMapper objectMapper;
    private final Tracer tracer;
    private final HttpClient httpClient;

    public DataSourceHttpClientService(ObjectMapper objectMapper, Tracer tracer, HttpClient httpClient) {
        this.objectMapper = objectMapper;
        this.tracer = tracer;
        this.httpClient = httpClient;
    }

    public Optional<List<Employee>> getAllEmployees() {
        LOGGER.info("Calling with HttpClient");
        HttpRequest httpRequest = createHttpRequest();
        return getEmployeesFromService(httpRequest);
    }

    private Optional<List<Employee>> getEmployeesFromService(HttpRequest httpRequest) {

        Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
        try {
            return httpClient
                    .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(this::deserializeBody)
                    .thenApply(Optional::ofNullable)
                    .thenApply(employees -> logSizesWithFixedMdc(employees, copyOfContextMap))
                    .get();
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("Failed to call datasource-service", e);
            throw new RuntimeException("Failed to call datasource-service");
        }
    }

    private Optional<List<Employee>> logSizesWithFixedMdc(Optional<List<Employee>> employees, Map<String, String> copyOfContextMap) {
        MDC.setContextMap(copyOfContextMap);
        LOGGER.info("The retrieved employee size: {}", employees.map(List::size).orElse(0));
        MDC.clear();
        return employees;
    }

    private HttpRequest createHttpRequest() {
        LOGGER.info("Setting OpenTrace headers");
        Optional<TraceContext> traceContext = getTraceContext();
        var httpRequestBuilder = HttpRequest.newBuilder(getDatasourceServiceUri());
        traceContext.map(TraceContext::traceId).ifPresent(traceId -> httpRequestBuilder.header("X-B3-TraceId", traceId));
//        traceContext.map(TraceContext::spanId).ifPresent(spanId -> httpRequestBuilder.header("X-B3-SpanId", tracer.nextSpan().context().spanId()));
        traceContext.map(TraceContext::traceId).ifPresent(traceId -> httpRequestBuilder.header("X-B3-ParentSpanId", traceId));
        traceContext.map(TraceContext::sampled).ifPresent(sampled -> httpRequestBuilder.header("X-B3-Sampled", sampled.toString()));
        return httpRequestBuilder.build();
    }

    private URI getDatasourceServiceUri() {
        try {
            return new URI("http://localhost:8082/employees");
        } catch (URISyntaxException e) {
            LOGGER.error("Failed to create URI", e);
            throw new RuntimeException("Failed to create DatasourceService URL");
        }
    }

    private Optional<TraceContext> getTraceContext() {
        return Optional.ofNullable(tracer.currentSpan()).map(Span::context);
    }

    private List<Employee> deserializeBody(String body) {
        try {
            LOGGER.info("Deserializing: {}", body);
            return objectMapper.readValue(body, new TypeReference<List<Employee>>() {
            });
        } catch (JsonProcessingException e) {
            LOGGER.error("Cannot Deserialize", e);
            return null;
        }
    }

}
