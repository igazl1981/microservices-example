package com.example.microservices.reader.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.sleuth.Span;
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

    public DatasourceResponse getSampleResponse() {
        try {
            LOGGER.info("Calling with HttpClient");
            Span span = tracer.currentSpan();
            var httpRequest = HttpRequest
                    .newBuilder(new URI("http://localhost:8082/"))
                    .header("X-B3-TraceId", span.context().traceId())
                    .header("X-B3-SpanId", span.context().spanId())
                    .build();
            return httpClient
                    .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(this::deserializeBody)
                    .get();
        } catch (URISyntaxException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    private DatasourceResponse deserializeBody(String body) {
        try {
            LOGGER.info("Deserializing: {}", body);
            return objectMapper.readValue(body, DatasourceResponse.class);
        } catch (JsonProcessingException e) {
            LOGGER.error("Cannot Deserialize", e);
            return null;
        }
    }

}
