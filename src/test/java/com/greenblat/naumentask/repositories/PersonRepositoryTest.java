package com.greenblat.naumentask.repositories;

import com.greenblat.naumentask.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class PersonRepositoryTest {

    @Autowired
    private PersonRepository underTest;

    @Test
    void itShouldFindPersonByName() {
        // Given
        String name = "Alex";
        Person person = new Person(null, name, 15, null);

        // When
        underTest.save(person);

        // Then
        Optional<Person> optionalPerson = underTest.findPersonByName(name);
        assertThat(optionalPerson)
                .isPresent()
                .isEqualTo(Optional.of(person));
    }

    @Test
    void isShouldNotFindPersonByNotExistName() {
        // Given
        String name = "Alex";
        Person person = new Person(null, name, 15, null);

        // When
        underTest.save(person);

        // Then
        String randomName = "random";
        Optional<Person> optionalPerson = underTest.findPersonByName(randomName);
        assertThat(optionalPerson).isNotPresent();

    }

    @Test
    void itShouldFindPersonWithMaxAge() {
        // Given
        int defaultAge = 10;
        int maxAge = 100;

        Person personWithDefaultAge = new Person(null, "Alex", defaultAge, null);
        Person personWithMaxAge1 = new Person(null, "Jim", maxAge, null);
        Person personWithMaxAge2 = new Person(null, "John", maxAge, null);

        List<Person> people = List.of(
                personWithMaxAge1,
                personWithMaxAge2
        );

        // When
        underTest.save(personWithDefaultAge);
        underTest.save(personWithMaxAge1);
        underTest.save(personWithMaxAge2);

        // Then
        List<Person> peopleWithMaxAge = underTest.findPeopleWithMaxAge();
        assertThat(peopleWithMaxAge).isEqualTo(people);
    }

    @Test
    void itShouldFindPersonWithMaxAgeByName() {
        // Given
        String nameWithMaxAge = "Jim";
        int defaultAge = 10;
        int maxAge = 100;
        Person personWithDefaultAge = new Person(null, "Alex", defaultAge, null);
        Person personWithMaxAge = new Person(null, nameWithMaxAge, maxAge, null);

        // When
        underTest.save(personWithDefaultAge);
        underTest.save(personWithMaxAge);

        // Then
        Integer maxAgeByName = underTest.findMaxAgeByName(nameWithMaxAge);
        assertThat(maxAgeByName).isEqualTo(maxAge);
    }
}