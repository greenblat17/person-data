package com.greenblat.naumentask.services;

import com.greenblat.naumentask.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public int getAgeByName(String name) {
        return personRepository.getAgeByName(name);
    }
}
