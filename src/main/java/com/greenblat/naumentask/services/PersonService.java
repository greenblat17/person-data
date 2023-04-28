package com.greenblat.naumentask.services;

import com.greenblat.naumentask.exception.EmptyFormException;
import com.greenblat.naumentask.model.Person;
import com.greenblat.naumentask.model.Statistics;
import com.greenblat.naumentask.model.dto.RestPersonDto;
import com.greenblat.naumentask.repositories.PersonRepository;
import com.greenblat.naumentask.repositories.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor()
public class PersonService {

    private final PersonRepository personRepository;
    private final StatisticsRepository statisticsRepository;
    private final RestTemplate restTemplate;

    @Value("${api.agify.url}")
    private String url;

    @Value("${person.age.default-value}")
    private  int defaultAge;

    @Value("${person.age.not-found-value}")
    private  int notFoundAge;

    @Value("${person.count.start-value}")
    private int startCount;

    @Transactional
    public int getPersonsAgeByName(String name) {
        if (name == null || name.isBlank()) {
            System.out.println("Excepttion");
            throw new EmptyFormException("name is empty");
        }

        Optional<Person> personByName = personRepository.findPersonByName(name);

        if (personByName.isEmpty()) {
            RestPersonDto personDto = getPersonWithNotFoundName(name);
            if (personDto.getAge() == notFoundAge) {
                personDto.setAge(defaultAge);
                personDto.setCount(startCount);
            }
            savePerson(personDto);

            return personDto.getAge();
        }

        Person person = personByName.get();
        statisticsRepository.updateCountByName(person.getName());

        return person.getAge();
    }

    public List<Person> getNamesWithMaxAge() {
        return personRepository.findPeopleWithMaxAge();
    }

    private RestPersonDto getPersonWithNotFoundName(String requestName) {
        return restTemplate.getForObject(url + requestName, RestPersonDto.class);
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