package com.greenblat.naumentask.repositories;

import com.greenblat.naumentask.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("SELECT p FROM Person p WHERE UPPER(p.name)=UPPER(:name) AND p.age=:age")
    Optional<Person> findPersonByNameAndAge(String name, int age);

    @Query(nativeQuery = true, value = "SELECT * FROM Person p WHERE UPPER(p.name)=UPPER(:name) ORDER BY p.id DESC LIMIT 1")
    Optional<Person> findPersonByName(@Param("name") String name);

    @Query("SELECT p " +
            "FROM Person p " +
            "WHERE p.age= (" +
                "SELECT MAX(p1.age) " +
                "FROM Person p1 " +
            ")"
    )
    List<Person> findPeopleWithMaxAge();

    @Query("SELECT p.age " +
            "FROM Person p " +
            "WHERE p.age= (" +
                "SELECT MAX(p1.age) " +
                "FROM Person p1 " +
                "WHERE UPPER(p1.name)=UPPER(:name)" +
            ")"
    )
    Integer findMaxAgeByName(@Param("name") String name);
}
