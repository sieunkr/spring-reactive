package com.example.demo.core;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class ProgrammerRepository {
    private static HashMap<String, Person> programmerHashMap = new HashMap<>();

    public Person findByName(String name){
        return programmerHashMap.get(name);
    }

    public List<Person> findAll(){
        //return programmerHashMap.values().stream().collect(Collectors.toList());
        return new ArrayList<>(programmerHashMap.values());
    }

    public void put(Person person){
        programmerHashMap.put(person.getName(), person);
    }
}
