package com.greenblat.naumentask.services;

import com.greenblat.naumentask.model.Person;
import com.greenblat.naumentask.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public List<Person> getAllPerson() {
        return personRepository.findAll();
    }

    public int getAgeByName(String name) {
        Optional<Person> personByName = personRepository.findPersonByName(name);
        if (personByName.isEmpty()) {
            return 0;
        }

        updateCountName(personByName.get());
        return personByName.get().getAge();
    }

    public List<String> getNameWithMaxAge() {
        return personRepository.findNameWithMaxAge();
    }

    private void updateCountName(Person person) {
        long curCount = person.getCount() == null ? 0 : person.getCount();
        person.setCount(curCount + 1);
        personRepository.save(person);
    }
}
