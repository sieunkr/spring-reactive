package com.example.demo;

import com.example.demo.core.Person;
import com.example.demo.core.ProgrammerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProgrammerControllerTests {

    @Autowired
    private ApplicationContext applicationContext;

    //@Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ProgrammerRepository programmerRepository;

    @BeforeEach
    public void 데이터_초기화(){
        webTestClient = WebTestClient.bindToApplicationContext(applicationContext).configureClient().build();
        programmerRepository.put(new Person("eddy", 1981));
    }

    @Test
    public void 정상적으로_응답하는가(){

        Person person = webTestClient.get().uri("/programmers/eddy").exchange()
                .expectStatus().isOk()
                .expectBody(Person.class)
                .returnResult().getResponseBody();

        assertEquals(person.getName(), "eddy");
        assertEquals(person.getBorn().intValue(), 1981);

    }

}
