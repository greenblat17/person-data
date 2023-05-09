package com.greenblat.naumentask.services;

import com.greenblat.naumentask.exception.EmptyNameException;
import com.greenblat.naumentask.model.Person;
import com.greenblat.naumentask.model.Statistics;
import com.greenblat.naumentask.model.dto.RestPersonDto;
import com.greenblat.naumentask.repositories.PersonRepository;
import com.greenblat.naumentask.repositories.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor()
public class PersonService {

    private final PersonRepository personRepository;
    private final StatisticsRepository statisticsRepository;
    private final RestTemplate restTemplate;
    private final Environment environment;

    private static final String AGIFY_URL_KEY = "api.agify.url";
    private static final String DEFAULT_AGE_KEY = "person.age.default-value";
    private static final String NOT_FOUND_AGE_KEY = "person.age.not-found-value";
    private static final String START_COUNT_KEY = "person.count.start-value";

    @Transactional
    public int getPersonsAgeByName(String name) {
        if (name == null || name.isBlank()) {
            throw new EmptyNameException("name is empty");
        }

        Optional<Person> personByName = personRepository.findPersonByName(name);

        if (personByName.isEmpty()) {
            RestPersonDto personDto = getPersonWithNotFoundName(name);

            setDefaultValuesToPersonDto(personDto);

            savePerson(personDto);

            return personDto.getAge();
        }

        Person person = personByName.get();
        statisticsRepository.updateCountByName(person.getName());

        return person.getAge();
    }

    private void setDefaultValuesToPersonDto(RestPersonDto personDto) {
        int notFoundAge = Integer.parseInt(environment.getRequiredProperty(NOT_FOUND_AGE_KEY));
        int defaultAge = Integer.parseInt(environment.getRequiredProperty(DEFAULT_AGE_KEY));
        int startCount = Integer.parseInt(environment.getRequiredProperty(START_COUNT_KEY));

        if (personDto.getAge() == notFoundAge) {
            personDto.setAge(defaultAge);
            personDto.setCount(startCount);
        }
    }

    private RestPersonDto getPersonWithNotFoundName(String requestName) {
        return restTemplate.getForObject(environment.getRequiredProperty(AGIFY_URL_KEY) + requestName, RestPersonDto.class);
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
        System.out.println(restPersonDto);
        return Person.builder()
                .age(restPersonDto.getAge())
                .name(restPersonDto.getName())
                .build();
    }
}