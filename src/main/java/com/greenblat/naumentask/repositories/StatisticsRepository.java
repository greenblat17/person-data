package com.greenblat.naumentask.repositories;

import com.greenblat.naumentask.model.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, Long> {

    @Query("SELECT s FROM Statistics s WHERE UPPER(s.person.name) = UPPER(:name)")
    List<Statistics> findStatisticsByPerson_Name(String name);
}
