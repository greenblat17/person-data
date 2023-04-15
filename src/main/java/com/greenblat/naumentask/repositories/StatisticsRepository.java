package com.greenblat.naumentask.repositories;

import com.greenblat.naumentask.model.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, Long> {

    List<Statistics> findStatisticsByPerson_Name(String name);
}
