package com.greenblat.naumentask.bootstrap;

import com.greenblat.naumentask.model.Person;
import com.greenblat.naumentask.reader.PersonReader;
import com.greenblat.naumentask.repositories.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BootStrapDataTest {

    @Mock
    private PersonRepository personRepository;
    @Mock
    private PersonReader personReader;
    @Captor
    private ArgumentCaptor<Person> personArgumentCaptor;

    @InjectMocks
    private BootStrapData underTest;

    @Test
    void runIfNameWithAgeNotExistsInDatabase() throws Exception {
        // Given
        String name = "Alex";
        int age = 20;
        List<Person> people = List.of(
                getPerson(name, age)
        );

        doReturn(people).when(personReader).readFile(anyString());
        doReturn(Optional.empty()).when(personRepository).findPersonByNameAndAge(name, age);

        // When
        underTest.run();

        // Then
        verify(personRepository).save(personArgumentCaptor.capture());

        Person personArgumentCaptorValue = personArgumentCaptor.getValue();

        assertThat(personArgumentCaptorValue.getName()).isEqualTo(name);
        assertThat(personArgumentCaptorValue.getAge()).isEqualTo(age);
        assertThat(personArgumentCaptorValue.getStatistics()).isNotNull();
        assertThat(personArgumentCaptorValue.getStatistics().getCount()).isEqualTo(0);
    }

    @Test
    void runIfNameWithAgeExistsInDatabase() throws Exception {
        // Given
        String name = "Alex";
        int age = 20;
        Person person = getPerson(name, age);

        List<Person> people = List.of(
                person
        );

        doReturn(people).when(personReader).readFile(anyString());
        doReturn(Optional.of(person)).when(personRepository).findPersonByNameAndAge(name, age);

        // When
        underTest.run();

        // Then

        verify(personRepository, never()).save(any(Person.class));
    }

    private Person getPerson(String name, int age) {
        return Person.builder()
                .name(name)
                .age(age)
                .build();
    }
}