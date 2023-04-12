package com.greenblat.naumentask.repositories;

import com.greenblat.naumentask.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    Person findByName(String name);

    @Query("SELECT p.age FROM Person p WHERE p.name=:name")
    int getAgeByName(String name);
}
