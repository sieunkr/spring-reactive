package com.example.demo.entrypoints;

import com.example.demo.core.Person;
import com.example.demo.core.ProgrammerUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class ProgrammerController {

    private final ProgrammerUseCase programmerUseCase;

    /*
    단일데이터 Mono
    리스트 Flux
     */

    @PostMapping("/init")
    public void init() {
        programmerUseCase.init();
    }

    @GetMapping("/programmers/{name}")
    public Mono<Person> hello(@PathVariable("name") String name) {
        return Mono.just(programmerUseCase.findByName(name));
    }

    //TODO:Person List 를 Flux<Person> 으로 변환하여 리턴
    @GetMapping("/programmers")
    public Flux<Person> all() {
        return null;
    }

}
