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
    private String url;

    @Value("${person.age.default_value}")
    private int defaultAge;

    public int getPersonsAgeByName(String name) {
        Optional<Person> personByName = personRepository.findPersonByName(name);

        if (personByName.isEmpty()) {
            RestPersonDto personDto = getPersonWithNotFoundName(name);
            if (personDto.getAge() == 0) {
                personDto.setAge(defaultAge);
                personDto.setCount(1);
            }
            savePerson(personDto);

            return personDto.getAge();
        }

        updateCountName(personByName.get());
        return personByName.get().getAge();
    }


    public List<Person> getNamesWithMaxAge() {
        return personRepository.findPersonWithMaxAge();
    }

    public Integer getMaxAgeByName(String name) {
        return personRepository.findPersonWithMaxAgeByName(name);
    }

    private RestPersonDto getPersonWithNotFoundName(String requestName) {
        return restTemplate.getForObject(url + requestName, RestPersonDto.class);
    }

    private void updateCountName(Person person) {
        Statistics statistics = person.getStatistics();
        long curCount = statistics.getCount();
        statistics.setCount(curCount + 1);
        statisticsRepository.save(statistics);
    }
    private void savePerson(RestPersonDto personDto) {
        Person person = Person.builder()
                .age(personDto.getAge())
                .name(personDto.getName().toLowerCase())
                .build();
        Statistics statistics = Statistics
                .builder()
                .count(personDto.getCount())
                .person(person)
                .build();

        person.setStatistics(statistics);

        personRepository.save(person);
        statisticsRepository.save(statistics);
    }
}
