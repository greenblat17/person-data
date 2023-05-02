package com.greenblat.naumentask.services;

import com.greenblat.naumentask.model.dto.ResponseStatisticsDto;
import com.greenblat.naumentask.repositories.PersonRepository;
import com.greenblat.naumentask.repositories.StatisticsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

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

        int count = 10;

        doReturn(count).when(statisticsRepository).findCountByPerson_Name(name);
        doReturn(maxAge).when(personRepository).findMaxAgeByName(name);

        // When
        ResponseStatisticsDto actualResult = statisticsService.getFullStatisticsByName(name);

        // Then
        verify(statisticsRepository).findCountByPerson_Name(name);
        verify(personRepository).findMaxAgeByName(name);

        ResponseStatisticsDto expectedResult = getStatisticsDto(count, maxAge);
        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    private ResponseStatisticsDto getStatisticsDto(int count, int age) {
        return new ResponseStatisticsDto(
                count,
                age
        );
    }

}