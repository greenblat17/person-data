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
            return getAgeForNotFoundName(name);
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

    private int getAgeForNotFoundName(String requestName) {
        RestPersonDto personDto = restTemplate.getForObject(url + requestName, RestPersonDto.class);
        return personDto != null ? personDto.getAge() : defaultAge;
    }

    private void updateCountName(Person person) {
        Statistics statistics = person.getStatistics();
        long curCount = statistics.getCount();
        statistics.setCount(curCount + 1);
        statisticsRepository.save(statistics);
    }
}
