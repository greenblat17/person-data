package com.greenblat.naumentask.repositories;

import com.greenblat.naumentask.model.Person;
import com.greenblat.naumentask.reader.PersonReader;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PersonRepository {

    private final List<Person> persons;

    public PersonRepository() {
        persons = new PersonReader().readFile();
    }

    public int findAgeByName(String name) {
        for (Person person : persons) {
            if (person.getName().equals(name)) {
                return person.getAge();
            }
        }
        return -1;
    }
}
