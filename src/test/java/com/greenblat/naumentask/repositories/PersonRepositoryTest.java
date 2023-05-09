package com.greenblat.naumentask.repositories;

import com.greenblat.naumentask.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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

        List<Person> expected = List.of(
                personWithMaxAge1,
                personWithMaxAge2
        );

        underTest.save(personWithDefaultAge);
        underTest.save(personWithMaxAge1);
        underTest.save(personWithMaxAge2);

        // When

        Page<Person> actual = underTest.findPeopleWithMaxAge(PageRequest.of(0, 5));

        // Then
        assertThat(actual.getContent()).isEqualTo(expected);
    }

    @Test
    void itShouldFindPersonWithMaxAgeIfPersonWithMaxAgeGreaterThanFive() {
        // Given
        int defaultAge = 10;
        int maxAge = 100;

        Person personWithDefaultAge = new Person(null, "Alex", defaultAge, null);
        Person personWithMaxAge1 = new Person(null, "Jim", maxAge, null);
        Person personWithMaxAge2 = new Person(null, "John", maxAge, null);
        Person personWithMaxAge3 = new Person(null, "John", maxAge, null);
        Person personWithMaxAge4 = new Person(null, "John", maxAge, null);
        Person personWithMaxAge5 = new Person(null, "John", maxAge, null);
        Person personWithMaxAge6 = new Person(null, "John", maxAge, null);

        List<Person> expectedSecondPage = List.of(
                personWithMaxAge6
        );

        underTest.save(personWithDefaultAge);
        underTest.save(personWithMaxAge1);
        underTest.save(personWithMaxAge2);
        underTest.save(personWithMaxAge3);
        underTest.save(personWithMaxAge4);
        underTest.save(personWithMaxAge5);
        underTest.save(personWithMaxAge6);

        // When

        Page<Person> actual = underTest.findPeopleWithMaxAge(PageRequest.of(1, 5));

        // Then
        assertThat(actual.getContent()).isEqualTo(expectedSecondPage);
    }

    @Test
    void itShouldReturnCountPeopleWithMaxAge() {
        // Given
        int defaultAge = 10;
        int maxAge = 100;

        Person personWithDefaultAge = new Person(null, "Alex", defaultAge, null);
        Person personWithMaxAge1 = new Person(null, "Jim", maxAge, null);
        Person personWithMaxAge2 = new Person(null, "John", maxAge, null);

        underTest.save(personWithDefaultAge);
        underTest.save(personWithMaxAge1);
        underTest.save(personWithMaxAge2);

        // When
        long actual = underTest.countPersonByMaxAge();

        // Then
        assertThat(actual).isEqualTo(2);
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