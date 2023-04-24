package com.greenblat.naumentask.services;

import com.greenblat.naumentask.model.Person;
import com.greenblat.naumentask.model.Statistics;
import com.greenblat.naumentask.repositories.PersonRepository;
import com.greenblat.naumentask.repositories.StatisticsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;
    @Mock
    private StatisticsRepository statisticsRepository;
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PersonService personService;

    @Test
    void itShouldGetAgeIfPersonWithNameDoesExist() {
        String name = "Alex";
        int age = 10;
        Person person = getPerson(name, age);
        doReturn(Optional.of(person)).when(personRepository).findPersonByName(name);

        // When
        int actualResult = personService.getPersonsAgeByName(name);

        // Then
        assertThat(actualResult).isEqualTo(age);
        verify(statisticsRepository).updateCountByName(name);
        verifyNoInteractions(restTemplate);
    }


    @Test
    void itShouldGetNamesWithMaxAge() {
        // Given
        int minAge = 10;
        int middleAge = 20;
        int maxAge = 30;

        Person personWithMinAge = getPerson("Alex", minAge);
        Person personWithMiddleAge = getPerson("Tim", middleAge);
        Person personWithMaxAge1 = getPerson("Gary", maxAge);
        Person personWithMaxAge2 = getPerson("Roy", maxAge);

        personRepository.saveAll(List.of(personWithMinAge, personWithMiddleAge, personWithMaxAge1, personWithMaxAge2));

        List<Person> peopleWithMaxAge = List.of(personWithMaxAge1, personWithMaxAge2);
        doReturn(peopleWithMaxAge).when(personRepository).findPeopleWithMaxAge();

        // When
        List<Person> actualResult = personService.getNamesWithMaxAge();

        // Then
        assertThat(actualResult).isNotEmpty();
        assertThat(actualResult).isEqualTo(peopleWithMaxAge);
    }

    private static Person getPerson(String name, int age) {
        return Person.builder()
                .name(name)
                .age(age)
                .build();
    }
}