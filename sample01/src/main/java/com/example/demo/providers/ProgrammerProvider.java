package com.example.demo.providers;

import com.example.demo.core.Person;
import com.example.demo.core.Personal;
import com.example.demo.core.ProgrammerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProgrammerProvider implements Personal {

    private final ProgrammerRepository programmerRepository;

    @Override
    public void init() {
        programmerRepository.put(new Person("eddy", 1981));
        programmerRepository.put(new Person("hye", 1982));
    }

    @Override
    public Person findByName(String name) {
        return programmerRepository.findByName(name);
    }

    @Override
    public List<Person> findAll() {
        return programmerRepository.findAll();
    }
}
