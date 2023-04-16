package com.greenblat.naumentask.bootstrap;

import com.greenblat.naumentask.model.Person;
import com.greenblat.naumentask.model.Statistics;
import com.greenblat.naumentask.reader.PersonReader;
import com.greenblat.naumentask.repositories.PersonRepository;
import com.greenblat.naumentask.repositories.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BootStrapData implements CommandLineRunner {

    private final PersonRepository personRepository;
    private final StatisticsRepository statisticsRepository;
    private final PersonReader personReader;

    @Value("${person.count.default_value}")
    private int defaultCount;

    @Override
    public void run(String... args) throws Exception {
        List<Person> people = personReader.readFile();
        for (Person person : people) {
            Statistics statistics = Statistics.builder()
                    .count(defaultCount)
                    .person(person)
                    .build();
            person.setStatistics(statistics);

            personRepository.save(person);
            statisticsRepository.save(statistics);
        }

    }
}
