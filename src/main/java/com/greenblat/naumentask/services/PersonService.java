package com.greenblat.naumentask.services;

import com.greenblat.naumentask.model.Person;
import com.greenblat.naumentask.model.dto.RestPersonDto;
import com.greenblat.naumentask.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final RestTemplate restTemplate;

    @Value("${api.agify.url}")
    private String url;

    public List<Person> getAllPerson() {
        return personRepository.findAll();
    }

    public int getPersonByName(String name) {
        Optional<Person> personByName = personRepository.findPersonByName(name);

        if (personByName.isEmpty()) {
            return getAgeForNotFoundName(name);
        }

        updateCountName(personByName.get());
        return personByName.get().getAge();
    }

    public List<String> getNamesWithMaxAge() {
        return personRepository.findNamesWithMaxAge();
    }

    private int getAgeForNotFoundName(String requestName) {
        RestPersonDto personDto = restTemplate.getForObject(url + requestName, RestPersonDto.class);
        return Objects.requireNonNull(personDto).getAge();
    }

    private void updateCountName(Person person) {
        long curCount = person.getCount() == null ? 0 : person.getCount();
        person.setCount(curCount + 1);
        personRepository.save(person);
    }
}
