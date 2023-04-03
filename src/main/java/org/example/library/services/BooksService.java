package org.example.library.services;

import org.example.library.model.Book;
import org.example.library.model.Person;
import org.example.library.repositories.BooksRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;

    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll(boolean sortByYear) {
        if (sortByYear)
            return booksRepository.findAll(Sort.by("yearOfWriting"));
        else
            return booksRepository.findAll();
    }

    public List<Book> findAll(Integer pageNumber, Integer pageSize, boolean sortByYear) {
        if(sortByYear)
            return booksRepository.findAll(PageRequest.of(pageNumber,pageSize,Sort.by("yearOfWriting"))).getContent();
        else
            return booksRepository.findAll(PageRequest.of(pageNumber,pageSize)).getContent();
    }

    public Book findById(int id) {
        Optional<Book> book = booksRepository.findById(id);
        return book.orElse(null);
    }

    public List<Book> findByName(String title) {
        return booksRepository.findByTitleStartingWith(title);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        Book bookToBeUpdated = booksRepository.findById(id).get();
        updatedBook.setBookId(id);
        updatedBook.setPerson(bookToBeUpdated.getPerson());
        booksRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public void addBookToPerson(int id, Person person) {
        Optional<Book> book = booksRepository.findById(id);
        if (book.isPresent()) {
            book.get().setPerson(person);
            book.get().setTakenAt(new Date());
            //booksRepository.save(book.orElse(null));
        }
    }

    public Person getOwnerBook(int id) {
        return booksRepository.getOwnerBook(id).orElse(null);
    }

    public Person getOwnerBook(String title) {
        return booksRepository.findByTitle(title).orElse(null);
    }

    @Transactional
    public void returnBook(int id) {
        booksRepository.findById(id).ifPresent(
                book -> {
                    book.setPerson(null);
                    book.setTakenAt(null);
                }
        );
        //booksRepository.returnBook(id);
    }

}
