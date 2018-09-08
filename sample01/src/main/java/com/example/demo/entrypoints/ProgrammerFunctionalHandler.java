package com.example.demo.entrypoints;

import com.example.demo.core.Person;
import com.example.demo.core.ProgrammerUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProgrammerFunctionalHandler {

    private final ProgrammerUseCase programmerUseCase;

    public Mono<ServerResponse> findByName(ServerRequest request) {
        Mono<Person> helloworldMono = Mono.just(programmerUseCase.findByName("eddy"));
        return ServerResponse.ok().body(helloworldMono, Person.class);
    }
}
