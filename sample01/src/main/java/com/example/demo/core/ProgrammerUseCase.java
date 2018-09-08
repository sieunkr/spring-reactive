package com.example.demo.core;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgrammerUseCase {

    private final Personal personal;

    public void init(){
        personal.init();
    }

    public Person findByName(String name){
        return personal.findByName(name);
    }

    public List<Person> findAll(){
        return personal.findAll();
    }

}
