package com.greenblat.naumentask.services;

import com.greenblat.naumentask.model.Person;
import com.greenblat.naumentask.model.dto.RestPersonDto;
import com.greenblat.naumentask.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
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

    public List<Person> getAllPerson() {
        return personRepository.findAll();
    }

    public int getAgeByName(String name) {
        Optional<Person> personByName = personRepository.findPersonByName(name);

        if (personByName.isEmpty()) {
            return getAgeForNotFoundName(name);
        }

        updateCountName(personByName.get());
        return personByName.get().getAge();
    }

    public List<String> getNameWithMaxAge() {
        return personRepository.findNameWithMaxAge();
    }

    private int getAgeForNotFoundName(String requestName) {
        String url = "https://api.agify.io/?name=" + requestName;
        RestPersonDto personDto = restTemplate.getForObject(url, RestPersonDto.class);
        return Objects.requireNonNull(personDto).getAge();
    }

    private void updateCountName(Person person) {
        long curCount = person.getCount() == null ? 0 : person.getCount();
        person.setCount(curCount + 1);
        personRepository.save(person);
    }
}
