package com.greenblat.naumentask.services;

import com.greenblat.naumentask.exception.EmptyFormException;
import com.greenblat.naumentask.model.Person;
import com.greenblat.naumentask.repositories.PersonRepository;
import com.greenblat.naumentask.repositories.StatisticsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    void itShouldThrownIfNameIsBlankOrNull() {
        String name = " ";
        assertThatThrownBy(() -> personService.getPersonsAgeByName(name))
                .isInstanceOf(EmptyFormException.class)
                .hasMessageContaining("name is empty");

        assertThatThrownBy(() -> personService.getPersonsAgeByName(null))
                .isInstanceOf(EmptyFormException.class)
                .hasMessageContaining("name is empty");
    }


    @Test
    void itShouldGetNamesWithMaxAge() {
        // When
        personService.getNamesWithMaxAge();

        // Then
        verify(personRepository).findPeopleWithMaxAge();
    }

    private Person getPerson(String name, int age) {
        return Person.builder()
                .name(name)
                .age(age)
                .build();
    }
}