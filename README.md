# Microservices sample

This project is an example project for building multiple microservices.

## Services

* Datasource
* Data reader
* Discovery server
* Gateway with zuul

The project uses Zipkin for OpenTracing which is integrated into the services.

### Zipkin

[Zipkin](https://zipkin.io/) is a distributed tracing system. To make tracing available the services uses `spring-cloud-starter-sleuth` for adding support of OpenTrace and `spring-cloud-sleuth-zipkin` for be able to send data to Zipkin.

## Actuator

Some services' actuator set to _open everything without any security_.

> DO NOT DO THIS IN PRODUCTION!

## Datasource and Data reader services

Both services are a standard Spring Boot (2.6.4) service.

The services are connected to the Discovery Server and register themselves with the name in their `spring.application.name` property.

In this sample the Eureka is configured on a custom port so the `eureka.client.service-url.defaultZone` has to be set to be able to connect.

The `@EnableEurekaClient` annotation has to be added to the class with `main` method and next to the `@SpringBootApplication` annotation.

### Datasource service

This service uses a MySql database for source and the database contains an [example database](https://www.mysqltutorial.org/mysql-sample-database.aspx).

#### Table names

The tables use cameCase in the DB. 

To force Spring Data to use the names from the `@Column` annotation the `spring.jpa.hibernate.naming.physical-strategy` property has to be set to `org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl`

For setting up the default database connection check the `spring.datasource` in [application.yml](./datasource-service/src/main/resources/application.yml).

### Data Reader service

This service uses the `Datasource` as a dependency and uses 2 methods to call it.

1. FeignClient
2. Java HttpClient

#### FeignClient

The `FeignClient` can use the Discovery Server to get the service name by setting `ribbon.eureka.enabled: true`.

#### HttpClient

The `HttpClient` requires some manual work for getting the service instance (`discoveryClient.getInstances("datasource-service")`) and adding the necessary headers to make OpenTracing work.

For OpenTracing the following headers has to be send `X-B3-TraceId`, `X-B3-ParentSpanId`, `X-B3-Sampled` (if the default names are used.)

## Discovery service

It is a default Eureka server which requires the `spring-cloud-starter-netflix-eureka-server` dependency and add `@EnableEurekaServer` annotation to the main class of the application.

Eureka is started on a custom port with `eureka.client.service-url.defaultZone` setting.

## Zuul with Spring boot

Using `spring-cloud-starter-netflix-zuul` needs Spring Boot 2.3.7 because this project became Spring Gateway.

Required annotation is `@EnableZuulProxy` for enabling the Zuul proxy.

For custom setup with Eureka and without it check the [application.yml](./gateway-zuul/src/main/resources/application.yml) and the `zuul.routes` setup.

Available routes are found on the [Gateway's actuator page](http://localhost:8754/actuator/routes)

### With Eureka

To use eureka for automatic mapping of registered services have to add `spring-cloud-starter-netflix-eureka-client` dependency and set `eureka.client.fetchRegistry: true`.

## Spring Cloud Gateway

> Spring MVC found on classpath, which is incompatible with Spring Cloud Gateway.

The Gateway is by default using reactive programming and if the `spring-boot-starter-web` is also used in the project the `spring.main.web-application-type=reactive` has to be set or remove `spring-boot-starter-web`.

Available routes are found on the [Gateway's actuator page](http://localhost:8854/actuator/gateway/routes)

The configuration can be in a [Configuration class](./gateway-cloud/src/main/java/com/example/microservices/gateway/cloud/config/RouteConfiguration.java) and in the [application.yml](./gateway-cloud/src/main/resources/application.yml) 's `spring.cloud.gateway.routes` node.

For more setups use [Spring Cloud Gateway reference](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/).


### With Eureka

Gateway is able to use Discovery Servers to expose routes.

For this the `spring.cloud.gateway.discovery.locator.enabled: true` has to be enabled and need to add `spring-cloud-starter-netflix-eureka-client` dependency.

> Some documentation says `@EnableEurekaClient` is also required but it's not necessary.

The routes are automatically set for the services from the Discovery Server with the `/<service-name>/**` and it is possible to use the service names in custom routes with `uri: lb://<service-name>` setup.    
