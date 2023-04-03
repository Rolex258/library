package org.example.library.dao;

import org.example.library.model.Book;
import org.example.library.model.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;


public class BookDAO {
    private final SessionFactory sessionFactory;

    public BookDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public List<Book> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Book", Book.class).getResultList();
        }
    }

    @Transactional(readOnly = true)
    public Optional<Book> getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Book.class, id));
        }
    }

    @Transactional(readOnly = true)
    public Optional<Book> getByTitle(String title) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Book.class, title));
        }
    }

    @Transactional
    public void save(Book book) {
        try (Session session = sessionFactory.openSession()) {
            session.save(book);
        }
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        try (Session session = sessionFactory.openSession()) {
            updatedBook.setBookId(id);
            session.update(updatedBook);
        }
    }

    @Transactional
    public void addBookToPerson(int id, Person person) {
        try (Session session = sessionFactory.openSession()) {
            Book book = session.get(Book.class, id);
            book.setPerson(person);
            session.update(book);
        }
    }

    @Transactional(readOnly = true)
    public Optional<Person> getOwnerBook(int id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.createQuery(
                            "select p from Person p Join Book b on p.personId=b.person.personId where b.person.personId=:id", Person.class)
                    .setParameter("id", id)
                    .getSingleResult());
        } catch (NoResultException e) {
        }
        return null;
    }

    @Transactional
    public void delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.remove(getById(id));
        }
    }

    @Transactional
    public void returnBook(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.createQuery("update Book b set b.person.books=null where b.bookId=:id", Book.class)
                    .setParameter("id", id)
                    .executeUpdate();
        }
    }
}
