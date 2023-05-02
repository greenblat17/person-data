package com.greenblat.naumentask.reader;

import com.greenblat.naumentask.exception.PersonFileNotFoundException;
import com.greenblat.naumentask.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PersonReaderTest {

    private String path;
    private List<Person> people;

    private PersonReader underTest;

    @BeforeEach
    void setUp() {
        path = "src/test/resources/";
        people = List.of(
                getPerson("AleX", 12),
                getPerson("Jack", 30),
                getPerson("Tom", 23)
        );
        underTest = new PersonReader();
    }

    @Test
    void itShouldReadFile() {
        // Given
        String file = path + "names.txt";

        // When
        List<Person> actualResult = underTest.readFile(file);

        // Then
        assertThat(actualResult).isNotEmpty();
        assertThat(actualResult).hasSameSizeAs(people);
        assertThat(actualResult).hasSameElementsAs(people);
    }

    @Test
    void itShouldThrownExceptionIfAgeIncorrect() {
        String file = path + "names-age-incorrect.txt";

        assertThatThrownBy(() -> underTest.readFile(file))
                .isInstanceOf(NumberFormatException.class)
                .hasMessageContaining("Age is in the wrong format");
    }

    @Test
    void itShouldThrownExceptionIfDataInWrongFormat() {
        String file = path + "names-wrong-format.txt";

        assertThatThrownBy(() -> underTest.readFile(file))
                .isInstanceOf(ArrayIndexOutOfBoundsException.class)
                .hasMessageContaining("The data in the file is in the wrong format");
    }

    @Test
    void itShouldThrownExceptionIfFileNotFound() {
        String file = path + "names-not-found.txt";

        assertThatThrownBy(() -> underTest.readFile(file))
                .isInstanceOf(PersonFileNotFoundException.class)
                .hasMessageContaining(String.format("File with name %s not found", file));
    }


    private Person getPerson(String name, int age) {
        return Person.builder()
                .name(name)
                .age(age)
                .build();
    }

}