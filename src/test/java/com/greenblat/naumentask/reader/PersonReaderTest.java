package com.greenblat.naumentask.reader;

import com.greenblat.naumentask.exception.PersonFileException;
import com.greenblat.naumentask.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class PersonReaderTest {

    private String path;
    private List<Person> people;

    @BeforeEach
    void setUp() {
        path = "src/test/resources/";
        people = List.of(
                getPerson("AleX", 12),
                getPerson("Jack", 30),
                getPerson("Tom", 23)
        );
    }

    @Test
    void itShouldReadFile() {
        // Given
        String file = path + "names.txt";
        List<Person> actualResult = new ArrayList<>();

        // When
        read(actualResult, file);

        // Then
        assertThat(actualResult).isNotEmpty();
        assertThat(actualResult).hasSameSizeAs(people);
        assertThat(actualResult).hasSameElementsAs(people);
    }

    @Test
    void itShouldThrownExceptionIfAgeIncorrect() {
        String file = path + "names-age-incorrect.txt";
        List<Person> actualResult = new ArrayList<>();

        assertThatThrownBy(() -> read(actualResult, file))
                .isInstanceOf(NumberFormatException.class)
                .hasMessageContaining("Age is in the wrong format");
    }

    @Test
    void itShouldThrownExceptionIfDataInWrongFormat() {
        String file = path + "names-wrong-format.txt";
        List<Person> actualResult = new ArrayList<>();

        assertThatThrownBy(() -> read(actualResult, file))
                .isInstanceOf(ArrayIndexOutOfBoundsException.class)
                .hasMessageContaining("The data in the file is in the wrong format");
    }

    @Test
    void itShouldThrownExceptionIfFileNotFound() {
        String file = path + "names-not-found.txt";
        List<Person> actualResult = new ArrayList<>();

        assertThatThrownBy(() -> read(actualResult, file))
                .isInstanceOf(PersonFileException.class)
                .hasMessageContaining(String.format("File with name %s not found", file));
    }


    private void read(List<Person> people, String fileName) {
        File file = new File(fileName);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] personInfo = line.split("_");
                Person person = getPerson(personInfo);

                people.add(person);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("The data in the file is in the wrong format");
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Age is in the wrong format");
        }  catch (IOException e) {
            throw new PersonFileException(String.format("File with name %s not found", fileName));
        }
    }

    private Person getPerson(String name, int age) {
        return Person.builder()
                .name(name)
                .age(age)
                .build();
    }

    private Person getPerson(String[] personInfo) {
        return Person.builder()
                .name(personInfo[0])
                .age(Integer.parseInt(personInfo[1]))
                .build();
    }
}