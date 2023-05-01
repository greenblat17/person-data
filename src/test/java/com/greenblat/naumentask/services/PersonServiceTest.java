package com.greenblat.naumentask.services;

import com.greenblat.naumentask.model.Person;
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
        verify(personRepository).findPeopleWithMaxAge();
    }

    private Person getPerson(String name, int age) {
        return Person.builder()
                .name(name)
                .age(age)
                .build();
    }
}