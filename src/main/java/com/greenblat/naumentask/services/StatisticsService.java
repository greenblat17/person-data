package com.greenblat.naumentask.services;

import com.greenblat.naumentask.model.Statistics;
import com.greenblat.naumentask.model.dto.ResponseStatisticsDto;
import com.greenblat.naumentask.repositories.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;
    private final PersonService personService;

    public List<Statistics> getStatisticsPeople() {
        return statisticsRepository.findAll();
    }

    public ResponseStatisticsDto getFullStatisticsByName(String name) {
        List<Statistics> statisticsByPersonName = statisticsRepository.findStatisticsByPerson_Name(name);

        int totalCount = 0;
        for (Statistics statistics : statisticsByPersonName) {
            totalCount += statistics.getCount();
        }

        Integer maxAge = personService.getMaxAgeByName(name);

        return ResponseStatisticsDto.builder()
                .count(totalCount)
                .age(maxAge)
                .build();
    }
}
