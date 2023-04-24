package com.greenblat.naumentask.repositories;

import com.greenblat.naumentask.model.Person;
import com.greenblat.naumentask.model.Statistics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StatisticsRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private StatisticsRepository underTest;

    @Test
    void itShouldFindCountByPerson_Name() {
        String name = "Alex";
        int count = 10;

        Person person1 = getPerson(name);
        Person person2 = getPerson(name);

        Statistics statistics1 = getStatistics(person1, count);
        person1.setStatistics(statistics1);

        Statistics statistics2 = getStatistics(person2, count);
        person2.setStatistics(statistics2);

        personRepository.save(person1);
        personRepository.save(person2);

        Integer actualResult = underTest.findCountByPerson_Name(name);

        assertThat(actualResult).isEqualTo(count);
    }

    @Test
    void itShouldNotFindCountByIfPerson_NameDoesNotExist() {
        String name = "Alex";

        Integer actualResult = underTest.findCountByPerson_Name(name);

        assertThat(actualResult).isNull();
    }

    @Test
    void itShouldUpdateCountByName() {
        // Given
        String name = "Alex";
        int count = 10;

        Person person1 = getPerson(name);
        Person person2 = getPerson(name);

        Statistics statistics1 = getStatistics(person1, count);
        person1.setStatistics(statistics1);

        Statistics statistics2 = getStatistics(person2, count);
        person2.setStatistics(statistics2);

        personRepository.save(person1);
        personRepository.save(person2);

        // When
        underTest.updateCountByName(name);

        // Then
        Integer count1 = person1.getStatistics().getCount();
        Integer count2 = person2.getStatistics().getCount();
        assertThat(count1).isEqualTo(count);
        assertThat(count2).isEqualTo(count);
    }

    private static Person getPerson(String name) {
        return Person.builder()
                .name(name)
                .age(9)
                .build();
    }

    private Statistics getStatistics(Person person, int count) {
        return Statistics.builder()
                .count(count)
                .person(person)
                .build();
    }
}