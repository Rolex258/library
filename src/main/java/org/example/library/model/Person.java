package org.example.library.model;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "Person")
public class Person {
    @Id
    @Column(name = "person_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int personId;
    @Column(name = "full_name")
    @NotEmpty(message = "Имя не должно быть пустым!")
    @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 100 символов")
    private String fullName;
    @Column(name = "year_of_birth")
    @Min(value = 1900, message = "Год рождения не ниже 1900!")
    @Max(value = 2018, message = "Год рождения не выше 2018!")
    private int yearOfBirth;

    @OneToMany(mappedBy = "person")
    private List<Book> books;

    public Person() {
    }

    public Person(String fullName, int yearOfBirth) {
        this.fullName = fullName;
        this.yearOfBirth = yearOfBirth;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "Person{" +
                "personId=" + personId +
                ", fullName='" + fullName + '\'' +
                ", yearOfBirth=" + yearOfBirth +
                ", books=" + books +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (personId != person.personId) return false;
        if (yearOfBirth != person.yearOfBirth) return false;
        return Objects.equals(fullName, person.fullName);
    }

    @Override
    public int hashCode() {
        int result = personId;
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        result = 31 * result + yearOfBirth;
        return result;
    }
}
