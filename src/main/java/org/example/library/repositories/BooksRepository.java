package org.example.library.repositories;

import org.example.library.model.Book;
import org.example.library.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> , PagingAndSortingRepository<Book, Integer> {
    @Query("Select p from Person p join Book b on p.personId=b.person.personId where b.bookId=:id")
    Optional<Person> getOwnerBook(@Param("id") int id);
    @Query("Select p from Person p join Book b on p.personId=b.person.personId where b.title=:title")
    Optional<Person> findByTitle(@Param("title") String title);

    @Query("UPDATE Book b set b.person=null where b.bookId=:id")
    @Modifying
    void returnBook(@Param("id") int id);
    @Query("from Book book where book.title like :name%")
    Optional<Book> findBookByTitle(@Param("name") String name);

    List<Book> findByTitleStartingWith(String title);



}
