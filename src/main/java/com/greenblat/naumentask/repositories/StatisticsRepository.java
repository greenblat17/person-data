package com.greenblat.naumentask.repositories;

import com.greenblat.naumentask.model.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, Long> {

    @Query("SELECT s.count " +
            "FROM Statistics s" +
            " WHERE UPPER(s.person.name) = UPPER(:name)" +
            " GROUP BY UPPER(:name), s.count")
    Integer findCountByPerson_Name(String name);

    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE statistics  s" +
                    " SET count = s.count + 1" +
                    " WHERE exists (SELECT * FROM person p WHERE UPPER(p.name) = UPPER(:name) AND p.statistics_id =s .id)")
    void updateCountByName(@Param("name") String name);

}
