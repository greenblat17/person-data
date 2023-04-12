package com.greenblat.naumentask.reader;

import com.greenblat.naumentask.model.Person;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonReader implements Reader<Person> {

    private static final String PATH = "src/main/resources/";

    public List<Person> readFile() {
        List<Person> persons = new ArrayList<>();

        File file = new File(PATH + "info.txt");
        try (BufferedReader reader = new BufferedReader(new java.io.FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] personInfo = line.split("_");
                Person person = getPerson(personInfo);
                persons.add(person);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(persons);
        return persons;
    }

    private static Person getPerson(String[] personInfo) {
        return Person.builder()
                .name(personInfo[0])
                .age(Integer.parseInt(personInfo[1]))
                .build();
    }
}
