package com.example.microservices.reader.client;

import com.fasterxml.jackson.annotation.JsonCreator;

public class DatasourceResponse {
    private final String message;

    @JsonCreator
    public DatasourceResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
