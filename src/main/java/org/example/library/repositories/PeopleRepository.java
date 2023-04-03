package org.example.library.repositories;

import org.example.library.model.Book;
import org.example.library.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    @Query("Select b from Book b where b.person.personId=:id")
    List<Book> findBooksByPersonId(@Param("id") int id);
    @Query("Select p from Person p where p.fullName=:name")
    Optional<Person> findByName(@Param("name") String name);

}
