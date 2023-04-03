package org.example.library.dao;

import org.example.library.model.Book;
import org.example.library.model.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public class PersonDAO {
    private final SessionFactory sessionFactory;

    public PersonDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public List<Person> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Person", Person.class).list();
        }

    }

    @Transactional(readOnly = true)
    public Optional<Person> getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Person.class, id));
        }


/*        return jdbcTemplate.query("SELECT * FROM Person WHERE person_id=?", new Object[]{id},
                        new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny();*/
    }

    @Transactional(readOnly = true)
    public List<Book> getBooksByPersonId(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Book b where b.person.id = :id", Book.class)
                    .setParameter("id", id)
                    .getResultList();
        }
    }

    @Transactional(readOnly = true)
    public Optional<Person> getByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.createQuery("select p from Person p where p.fullName=:name", Person.class)
                    .setParameter("name", name)
                    .getSingleResult());

        }
    }

    @Transactional
    public void save(Person person) {
        try (Session session = sessionFactory.openSession()) {
            session.saveOrUpdate(person);
        }
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        try (Session session = sessionFactory.openSession()) {
            Person person = session.get(Person.class, id);

            person.setFullName(updatedPerson.getFullName());
            person.setYearOfBirth(updatedPerson.getYearOfBirth());
            person.setBooks(updatedPerson.getBooks());

            session.update(person);

        }
    }
    @Transactional
    public void delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.remove(session.get(Person.class, id));
        }
    }
}
