package com.greenblat.naumentask.services;

import com.greenblat.naumentask.model.Statistics;
import com.greenblat.naumentask.model.dto.ResponseStatisticsDto;
import com.greenblat.naumentask.repositories.PersonRepository;
import com.greenblat.naumentask.repositories.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;
    private final PersonRepository personRepository;

    public List<Statistics> getStatisticsPeople() {
        return statisticsRepository.findAll();
    }

    public ResponseStatisticsDto getFullStatisticsByName(String name) {
        Integer count = statisticsRepository.findCountByPerson_Name(name);
        Integer maxAge = personRepository.findMaxAgeByName(name);
        return ResponseStatisticsDto.builder()
                .count(count)
                .age(maxAge)
                .build();
    }
}
