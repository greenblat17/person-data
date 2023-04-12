package com.greenblat.naumentask.bootstrap;

import com.greenblat.naumentask.model.Person;
import com.greenblat.naumentask.reader.PersonReader;
import com.greenblat.naumentask.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BootStrapData implements CommandLineRunner {

    private final PersonRepository personRepository;
    private final PersonReader personReader;

    @Override
    public void run(String... args) throws Exception {
        List<Person> people = personReader.readFile();
        personRepository.saveAll(people);
    }
}
