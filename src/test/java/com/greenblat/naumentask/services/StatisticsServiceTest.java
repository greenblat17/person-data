package com.greenblat.naumentask.services;

import com.greenblat.naumentask.model.Person;
import com.greenblat.naumentask.model.dto.ResponseStatisticsDto;
import com.greenblat.naumentask.repositories.PersonRepository;
import com.greenblat.naumentask.repositories.StatisticsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceTest {

    @Mock
    private PersonRepository personRepository;
    @Mock
    private StatisticsRepository statisticsRepository;

    @InjectMocks
    private StatisticsService statisticsService;

    @Test
    void itShouldGetFullStatisticsByName() {
        // Given
        String name = "Alex";
        int maxAge = 100;
        int defaultAge = 10;
        Person person1 = getPerson(name, maxAge);
        Person person2 = getPerson(name, defaultAge);
        Person personWithAnotherName = getPerson(anyString(), maxAge);

        int count = 10;

        personRepository.saveAll(List.of(person1, person2, personWithAnotherName));

        doReturn(count).when(statisticsRepository).findCountByPerson_Name(name);
        doReturn(maxAge).when(personRepository).findMaxAgeByName(name);

        // When
        ResponseStatisticsDto actualResult = statisticsService.getFullStatisticsByName(name);

        // Then
        ResponseStatisticsDto expectedResult = getStatisticsDto(count, maxAge);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    private ResponseStatisticsDto getStatisticsDto(int count, int age) {
        return ResponseStatisticsDto.builder()
                .age(age)
                .count(count)
                .build();
    }

    private Person getPerson(String name, int age) {
        return Person.builder()
                .name(name)
                .age(age)
                .build();
    }
}