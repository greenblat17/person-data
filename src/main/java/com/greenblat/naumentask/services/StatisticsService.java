package com.greenblat.naumentask.services;

import com.greenblat.naumentask.model.Statistics;
import com.greenblat.naumentask.repositories.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;

    public List<Statistics> getStatisticsPeople() {
        return statisticsRepository.findAll();
    }
}
