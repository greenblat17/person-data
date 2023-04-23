package com.greenblat.naumentask.services;

import com.greenblat.naumentask.model.Person;
import com.greenblat.naumentask.model.Statistics;
import com.greenblat.naumentask.model.dto.RestPersonDto;
import com.greenblat.naumentask.repositories.PersonRepository;
import com.greenblat.naumentask.repositories.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final StatisticsRepository statisticsRepository;
    private final RestTemplate restTemplate;

    @Value("${api.agify.url}")
    private final String URL;

    @Value("${person.age.default_value}")
    private final int DEFAULT_AGE;

    @Value("${person.age.not_found_value}")
    private final int NOT_FOUND_AGE;

    @Value("${person.count.start_value}")
    private final int START_COUNT;

    public int getPersonsAgeByName(String name) {
        Optional<Person> personByName = personRepository.findPersonByName(name);

        if (personByName.isEmpty()) {
            RestPersonDto personDto = getPersonWithNotFoundName(name);
            if (personDto.getAge() == NOT_FOUND_AGE) {
                personDto.setAge(DEFAULT_AGE);
                personDto.setCount(START_COUNT);
            }
            savePerson(personDto);

            return personDto.getAge();
        }

        updateCountName(personByName.get());
        return personByName.get().getAge();
    }


    public List<Person> getNamesWithMaxAge() {
        return personRepository.findPeopleWithMaxAge();
    }

    public Integer getMaxAgeByName(String name) {
        return personRepository.findPersonsAgeWithMaxAgeByName(name);
    }

    private RestPersonDto getPersonWithNotFoundName(String requestName) {
        return restTemplate.getForObject(URL + requestName, RestPersonDto.class);
    }

    private void updateCountName(Person person) {
        Statistics statistics = person.getStatistics();
        long curCount = statistics.getCount();
        statistics.setCount(curCount + 1);
        statisticsRepository.save(statistics);
    }

    private void savePerson(RestPersonDto personDto) {
        Person person = restPersonDtoMapToPerson(personDto);

        Statistics statistics = Statistics
                .builder()
                .count(personDto.getCount())
                .person(person)
                .build();

        person.setStatistics(statistics);

        personRepository.save(person);
    }

    private Person restPersonDtoMapToPerson(RestPersonDto restPersonDto) {
        return Person.builder()
                .age(restPersonDto.getAge())
                .name(restPersonDto.getName())
                .build();
    }
}