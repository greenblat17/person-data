package com.greenblat.naumentask.services;

import com.greenblat.naumentask.exception.EmptyNameException;
import com.greenblat.naumentask.model.Person;
import com.greenblat.naumentask.model.dto.RestPersonDto;
import com.greenblat.naumentask.repositories.PersonRepository;
import com.greenblat.naumentask.repositories.StatisticsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
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
    @Mock
    private Environment environment;
    @Captor
    private ArgumentCaptor<Person> personArgumentCaptor;

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
                .isInstanceOf(EmptyNameException.class)
                .hasMessageContaining("name is empty");

        assertThatThrownBy(() -> personService.getPersonsAgeByName(null))
                .isInstanceOf(EmptyNameException.class)
                .hasMessageContaining("name is empty");
    }

    @Test
    void itShouldSavePersonIfPersonWithNameDoesNotExistInDatabaseAndExistInAgify() {
        // Given
        String name = "Alex";
        doReturn(Optional.empty()).when(personRepository).findPersonByName(name);

        // .. Get personDto from Agify
        int count = 10;
        int age = 20;
        RestPersonDto personDto = getPersonDto(count, age, name);

        // .. Stubs env variables
        doReturn("https://api.agify.io?name=").when(environment).getRequiredProperty("api.agify.url");
        doReturn("0").when(environment).getRequiredProperty("person.age.not-found-value");
        doReturn("1000").when(environment).getRequiredProperty("person.age.default-value");
        doReturn("1").when(environment).getRequiredProperty("person.count.start-value");

        doReturn(personDto).when(restTemplate)
                .getForObject("https://api.agify.io?name=" + name, RestPersonDto.class);

        // When
        int actualAge = personService.getPersonsAgeByName(name);

        // Then
        assertThat(actualAge).isEqualTo(age);

        verify(personRepository).save(any(Person.class));

        verify(personRepository).save(personArgumentCaptor.capture());
        Person personArgumentCaptorValue = personArgumentCaptor.getValue();

        assertThat(personArgumentCaptorValue.getName()).isEqualTo(name);
        assertThat(personArgumentCaptorValue.getAge()).isEqualTo(age);
        assertThat(personArgumentCaptorValue.getStatistics()).isNotNull();
        assertThat(personArgumentCaptorValue.getStatistics().getCount()).isEqualTo(count);
    }

    @Test
    void itShouldSavePersonIfPersonWithNameDoesNotExistInDatabaseAndNotExistInAgify() {
        // Given
        String name = "Alex";
        doReturn(Optional.empty()).when(personRepository).findPersonByName(name);

        // .. Get personDto from Agify
        RestPersonDto personDto = getPersonDto(0, 0, name);

        // .. Stubs env variables
        doReturn("https://api.agify.io?name=").when(environment).getRequiredProperty("api.agify.url");
        doReturn("0").when(environment).getRequiredProperty("person.age.not-found-value");
        doReturn("1000").when(environment).getRequiredProperty("person.age.default-value");
        doReturn("1").when(environment).getRequiredProperty("person.count.start-value");

        doReturn(personDto).when(restTemplate)
                .getForObject("https://api.agify.io?name=" + name, RestPersonDto.class);

        // When
        int actualAge = personService.getPersonsAgeByName(name);

        // Then
        assertThat(actualAge).isEqualTo(1000);

        verify(personRepository).save(any(Person.class));

        verify(personRepository).save(personArgumentCaptor.capture());
        Person personArgumentCaptorValue = personArgumentCaptor.getValue();

        assertThat(personArgumentCaptorValue.getName()).isEqualTo(name);
        assertThat(personArgumentCaptorValue.getAge()).isEqualTo(1000);
        assertThat(personArgumentCaptorValue.getStatistics()).isNotNull();
        assertThat(personArgumentCaptorValue.getStatistics().getCount()).isEqualTo(1);
    }


    private Person getPerson(String name, int age) {
        return Person.builder()
                .name(name)
                .age(age)
                .build();
    }

    private RestPersonDto getPersonDto(int count, int age, String name) {
        return RestPersonDto.builder()
                .name(name)
                .count(count)
                .age(age)
                .build();
    }
}