package com.gagan.microservices.apigateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {

    @Bean
    public RouteLocator getewayRouter(RouteLocatorBuilder builder) {
        // The '/get' Url is just for demo purpose, how you can add additional data at the Geteway
        return builder.routes()
                .route(p -> p.path("/get")
                        .filters(f -> f.addRequestHeader("myHeader", "myUrl")
                                .addRequestParameter("myParam", "myValue"))
                        .uri("http://httpbin.org:80"))
                .route(p -> p.path("/curreny-exchange/**")
                        .uri("lb://currency-exchange")) // Talk to Eureka Naming Server, find the location on this 'currency-exchange' service and load balance
                .route(p -> p.path("/currency-conversion/**")
                        .uri("lb://currency-conversion"))
                .route(p -> p.path("/currency-conversion-feign/**")
                        .uri("lb://currency-conversion"))
                .route(p -> p.path("/currency-conversion-new/**")
                        .filters(f -> f.rewritePath("/currency-conversion-new/(?<segment>.*)", "/currency-conversion-feign/${segment}"))
                        .uri("lb://currency-conversion")) // Thi route is just an examle how you can do Url rewriting
                .build();
    }
}
