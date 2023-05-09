package com.greenblat.naumentask.services;

import com.greenblat.naumentask.model.Person;
import com.greenblat.naumentask.model.Statistics;
import com.greenblat.naumentask.model.dto.ResponseStatisticsDto;
import com.greenblat.naumentask.repositories.PersonRepository;
import com.greenblat.naumentask.repositories.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;
    private final PersonRepository personRepository;

    @Value("${spring.data.web.pageable.default-page-size}")
    private int pageSize;

    public Long getCountStatisticsInDb() {
        return statisticsRepository.count();
    }

    public Page<Statistics> getStatisticsPeople(Integer page) {
        return statisticsRepository.findAll(PageRequest.of(page, pageSize, Sort.by("id").ascending()));
    }

    public ResponseStatisticsDto getFullStatisticsByName(String name) {
        Integer count = statisticsRepository.findCountByPerson_Name(name);
        Integer maxAge = personRepository.findMaxAgeByName(name);
        return new ResponseStatisticsDto(count, maxAge);
    }

    public Page<Person> getNamesWithMaxAge(int page) {
        return personRepository.findPeopleWithMaxAge(PageRequest.of(page, pageSize, Sort.by("id").ascending()));
    }

    public Long getCountPeopleWithMaxAge() {
        return personRepository.countPersonByMaxAge();
    }
}
