package com.example.microservices.gateway.cloud.config;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfiguration {

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder
                .routes()
                .route(this::getDataReaderUri)
                .build();
    }

    private Buildable<Route> getDataReaderUri(PredicateSpec predicateSpec) {
        return predicateSpec
                .path("/reader-java-config/**")
                .filters(gatewayFilterSpec -> gatewayFilterSpec.stripPrefix(1))
                .uri("http://localhost:8081/");
    }
}
