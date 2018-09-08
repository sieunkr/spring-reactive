package com.example.demo.core;

import java.util.List;

public interface Personal {
    void init();
    Person findByName(String name);
    List<Person> findAll();
}
