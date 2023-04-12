package com.greenblat.naumentask.repositories;

import com.greenblat.naumentask.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("SELECT p.age FROM Person p WHERE p.name=:name")
    int getAgeByName(String name);

    @Query("SELECT p.name " +
            "FROM Person p " +
            "WHERE p.age= (" +
                "SELECT MAX(p1.age) " +
                "FROM Person p1 " +
            ")"
    )
    List<String> findNameWithMaxAge();
}
