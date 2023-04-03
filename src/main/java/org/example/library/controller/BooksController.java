package org.example.library.controller;

import org.example.library.model.Book;
import org.example.library.model.Person;
import org.example.library.services.BooksService;
import org.example.library.services.PeopleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final BooksService booksService;
    private final PeopleService peopleService;

    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;

    }

    @GetMapping()
    public String findAll(Model model, @RequestParam(value = "page", required = false) Integer page,
                          @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                          @RequestParam(value = "sort_by_year", required = false) boolean sortByYear ) {
        if(page==null || booksPerPage==null)
            model.addAttribute("books", booksService.findAll(sortByYear));

        else model.addAttribute("books", booksService.findAll(page, booksPerPage, sortByYear));
        return "books/findall";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable int id, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book", booksService.findById(id));
        Optional<Person> owner = Optional.ofNullable(booksService.getOwnerBook(id));
        if (owner.isPresent()) model.addAttribute("owner", owner.get());
        else model.addAttribute("people", peopleService.findAll());
        return "books/findById";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "books/new";
        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable int id, Model model) {
        model.addAttribute("book", booksService.findById(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable int id, @ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "books/edit";
        booksService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/givebook")
    public String giveBook(@PathVariable int id, @ModelAttribute("person") Person person) {
        booksService.addBookToPerson(id, person);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/takebook")
    public String takeBook(@PathVariable int id) {
        booksService.returnBook(id);
        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String search(@ModelAttribute("book") Book book) {
        return "books/search";
    }
    @PostMapping("/search")
    public String makeSearch(Model model, @RequestParam("query") String query){
        model.addAttribute("books", booksService.findByName(query));
        return "books/search";
    }

}
