package com.greenblat.naumentask.services;

import com.greenblat.naumentask.model.Person;
import com.greenblat.naumentask.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository personRepository;

    public int getAgeByName(String name) {
        Optional<Person> personByName = personRepository.findPersonByName(name);
        return personByName.map(Person::getAge).orElse(0);

    }

    public List<String> getNameWithMaxAge() {
        return personRepository.findNameWithMaxAge();
    }
}
