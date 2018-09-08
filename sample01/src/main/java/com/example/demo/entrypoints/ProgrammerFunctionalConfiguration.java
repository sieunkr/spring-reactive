package com.example.demo.entrypoints;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
@EnableWebFlux
public class ProgrammerFunctionalConfiguration {

    @Bean
    public RouterFunction<ServerResponse> routes(ProgrammerFunctionalHandler handler) {
        return RouterFunctions.route(GET("/personfunctional"), handler::findByName);
    }
}
